/*
 * Copyright (c) 1993-1999 David Gay and Gustav H�llberg
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software for any
 * purpose, without fee, and without written agreement is hereby granted,
 * provided that the above copyright notice and the following two paragraphs
 * appear in all copies of this software.
 * 
 * IN NO EVENT SHALL DAVID GAY OR GUSTAV HALLBERG BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF DAVID GAY OR
 * GUSTAV HALLBERG HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * DAVID GAY AND GUSTAV HALLBERG SPECIFICALLY DISCLAIM ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN
 * "AS IS" BASIS, AND DAVID GAY AND GUSTAV HALLBERG HAVE NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

#include <string.h>
#include <stdlib.h>
#include <stddef.h>
#include "mudlle.h"
#include "interpret.h"
#include "alloc.h"
#include "types.h"
#include "code.h"
#include "global.h"
#include "error.h"
#include "call.h"
#include "runtime/runtime.h"
#include "runtime/stringops.h"
#include "runtime/symbol.h"
#include "table.h"

#ifndef TINY
/* As good a place as any other */
const char *COPYRIGHT = "\
Copyright (c) 1993-1999 David Gay and Gustav H�llberg\n\
All rights reserved.\n\
\n\
Permission to use, copy, modify, and distribute this software for any\n\
purpose, without fee, and without written agreement is hereby granted,\n\
provided that the above copyright notice and the following two paragraphs\n\
appear in all copies of this software.\n\
\n\
IN NO EVENT SHALL DAVID GAY OR GUSTAV HALLBERG BE LIABLE TO ANY PARTY FOR\n\
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT\n\
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF DAVID GAY OR\n\
GUSTAV HALLBERG HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n\
\n\
DAVID GAY AND GUSTAV HALLBERG SPECIFICALLY DISCLAIM ANY WARRANTIES,\n\
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n\
FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN\n\
\"AS IS\" BASIS, AND DAVID GAY AND GUSTAV HALLBERG HAVE NO OBLIGATION TO\n\
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.\n\
";
#endif

/* Macros for fast access to the GC'ed stack & code structures.
   RESTORE_INS must be called after anything that may have caused a GC
*/

#define RESTORE_SP() (stkpos = (value *)sp)
#define SAVE_SP() (sp = (u8 *)stkpos)

#define FAST_RESERVE(n) (stkpos - (n) < (value *)splimit ? (SAVE_SP(), SAVE_INS(), stack_reserve((n) * sizeof(value)), RESTORE_INS()) : 0)
#define FAST_POP() (*stkpos++)
#define FAST_POPN(n) ((void)(stkpos += (n)))
#define FAST_PUSH(v) do { *--stkpos = (v); } while(0)
#define FAST_GET(n) (stkpos[(n)])
#define FAST_SET(n, v) (stkpos[(n)] = (v))

#define RESTORE_INS() (ins = &frame->fn->code->ins[frame->ins])
#define SAVE_INS() (frame->ins = ins - frame->fn->code->ins)

#define INSU8() (*ins++)
#define INSI8() ((i8)INSU8())
#define INSU16() (byte1 = *ins++, (byte1 << 8) + *ins++)
#define INSI16() ((i16)INSU16())
#define INSSKIP(n) (ins += (n))

#define INSCST() (ins += sizeof(value), RINSCST(ins - sizeof(value)))

struct interpret_frame {
  struct generic_frame g;
  u16 ins;
#ifndef TINY
  u32 start_ins;
#endif
  struct closure *fn;
  struct variable *locals[1/*n*/];
};

u8 ins_size(instruction i)
/* Requires: i != op_closure */
{
  /* Code rather than array, for now at least */
  if (i >= op_typecheck)
    return 2;

  if (i >= op_first_builtin)
    return 1;

  switch (i)
    {
    case op_return: case op_discard:
      return 1;

    case op_constant:
      return 1 + sizeof(value);

    case op_integer1: case op_execute: case op_exit_n:
    case op_branch1: case op_loop1: case op_branch_nz1: case op_branch_z1:
    op_clear_local: case op_recall + local_var: case op_recall + closure_var:
    case op_assign + local_var: case op_assign + closure_var:
      return 2;

    case op_execute_global1: case op_execute_global2: case op_branch2:
    case op_loop2: case op_branch_nz2: case op_branch_z2:
    case op_recall + global_var: case op_assign + global_var: case op_define:
      return 3;

    default:
      assert(0);
      return 1;
    }
}

#ifndef TINY
#include "print.h"

static u32 instruction_number;
static i8 actual_args = -1;

static void print_bytecode(struct interpret_frame *frame)
{
  struct closure *fn = frame->fn;

  if (fn->code->filename->str[0])
    {
      i8 i, nargs;
      uvalue first_arg_offset;
      bool first = TRUE;

      print_fnname(muderr, fn);
      mputs("(", muderr);
      first_arg_offset = frame->fn->code->nb_locals - 1;
      nargs = frame->fn->code->nargs;

      /* Evil hack part 2 (see push_closure for part 1) */
      if (actual_args >= 0)
	{
	  first_arg_offset += actual_args - nargs;
	  nargs = actual_args;
	}

      if (nargs < 0)
	nargs = 1; /* varargs case */

      for (i = 0; i < nargs; i++)
	{
	  value arg;

	  if (!first)
	    mputs(", ", muderr);
	  first = FALSE;

	  arg = frame->locals[first_arg_offset - i];
	  if (TYPE(arg, itype_variable))
	    arg = ((struct variable *)arg)->vvalue;
	  mprint(muderr, prt_print, arg);
	}
      mputs(")" EOL, muderr);
    }
  /* Only meaningful in the first frame */
  actual_args = -1;
}
#endif

static value code_ref(value x1, value x2)
{
  if (!POINTERP(x1))
    RUNTIME_ERROR(error_bad_type);
  switch (OBJTYPE(x1))
    {
    case type_vector: {
      struct vector *vec = x1;
      ivalue idx;

      ISINT(x2);
      idx = intval(x2);
      if (idx < 0 || idx >= vector_len(vec))
	RUNTIME_ERROR(error_bad_index);
      return (vec->data[idx]);
    }
#ifndef TINY
    case type_string:
      return code_string_ref(x1, x2);
    case type_table: {
      struct symbol *sym;
      struct string *s = x2;

      TYPEIS(s, type_string);
      if (!table_lookup(x1, s->str, &sym)) return NULL;
      return sym->data;
    }
#endif
    default: RUNTIME_ERROR(error_bad_type);
    }
}

static value code_set(value x1, value x2, value x3)
{
  if (!POINTERP(x1))
    RUNTIME_ERROR(error_bad_type);
  switch (OBJTYPE(x1))
    {
    case type_vector: {
      struct vector *vec = x1;
      ivalue idx;

      if (readonlyp(vec))
	RUNTIME_ERROR(error_value_read_only);
      ISINT(x2);
      idx = intval(x2);

      if (idx < 0 || idx >= vector_len(vec))
	RUNTIME_ERROR(error_bad_index);
      vec->data[idx] = x3;
      return x3;
    }
#ifndef TINY
    case type_string:
      return code_string_set(x1, x2, x3);
    case type_table:
      return table_mset(x1, x2, x3);
#endif
    default: RUNTIME_ERROR(error_bad_type);
    }
}

static CC execute_bytecode(struct interpret_frame *frame)
{
  instruction *ins, byteop;
  u16 coffset;
  u8 byte1;
  value *stkpos;
  value arg1, arg2, result;
  struct obj *called;
  struct primitive_ext *op;
  u8 nargs;

  RESTORE_SP();
  RESTORE_INS();

 nextins:
#ifndef TINY
  instruction_number++;
#endif
  MDBG8(dbg_ins); MDBG16(ins);
  byteop = INSU8();
   MDBG8(byteop); MDBG16(stkpos);

  switch (byteop)
    {
    case op_first_twoint_args ... op_last_twoint_args:
    {
      ivalue i1, i2;

      arg2 = FAST_POP();
      arg1 = FAST_GET(0);
      if (!(INTEGERP(arg1) && INTEGERP(arg2)))
	runtime_error(error_bad_type); 
      i1 = intval(arg1); i2 = intval(arg2);
      switch (byteop)
	{
	default:
	  assert(0);
	  result = NULL;
	  break;
	case op_builtin_lt:
	  result = makebool((ivalue)arg1 < (ivalue)arg2);
	  break;
	case op_builtin_le:
	  result = makebool((ivalue)arg1 <= (ivalue)arg2);
	  break;
	case op_builtin_gt:
	  result = makebool((ivalue)arg1 > (ivalue)arg2);
	  break;
	case op_builtin_ge:
	  result = makebool((ivalue)arg1 >= (ivalue)arg2);
	  break;

	case op_builtin_sub:
	  result = (value)((ivalue)arg1 - (ivalue)arg2 + 1);
	  break;
	case op_builtin_multiply:
	  result = (makeint(intval(arg1) * intval(arg2)));
	  break;
	case op_builtin_divide:
	case op_builtin_remainder:
	  if (i2 == 0)
	    runtime_error(error_divide_by_zero);
	  result = makeint(byteop == op_builtin_divide ? i1 / i2 : i1 % i2);
	  break;

	case op_builtin_bitand:
	  result = (value)((ivalue)arg1 & (ivalue)arg2);
	  break;
	case op_builtin_bitor:
	  result = (value)((ivalue)arg1 | (ivalue)arg2);
	  break;
	case op_builtin_bitxor:
	  result = (value)((ivalue)arg1 ^ (ivalue)arg2 | 1);
	  break;

	case op_builtin_shift_left:
	  result = makeint(intval(arg1) << intval(arg2));
	  break;
	case op_builtin_shift_right:
	  result = makeint(intval(arg1) >> intval(arg2));
	  break;
	}
      FAST_SET(0, result);
      break;
    }
    case op_return: 
#ifndef TINY
      /* Profiling */
      frame->fn->code->instruction_count += instruction_number - frame->start_ins;
#endif
      result = FAST_GET(0);
      SAVE_SP();
      FA_POP(&fp, &sp);
      stack_push(result);
      return;
    case op_constant:
      FAST_RESERVE(1);
      FAST_PUSH(INSCST());
      GCCHECK(FAST_GET(0));
      break;
    case op_integer1:
      FAST_RESERVE(1);
      FAST_PUSH(makeint(INSI8()));
      break;

    case op_closure:
      {
	u8 nvars = INSU8(), var;
	struct closure *new_closure;

	SAVE_SP(); SAVE_INS();
	new_closure = unsafe_alloc_and_push_closure(nvars);
	RESTORE_INS(); RESTORE_SP();

	for (var = 0; var < nvars; var++)
	  {
	    u8 varspec = INSU8();
	    u8 whichvar = varspec >> 1;
	    struct variable *v;

	    if ((varspec & 1) == local_var)
	      v = frame->locals[frame->fn->code->nb_locals - 1 - whichvar];
	    else
	      v = frame->fn->variables[whichvar];

	    new_closure->variables[var] = v;
	  }
	new_closure->code = INSCST();
	break;
      }

    case op_execute_global1: 
      called = GVAR(globals, INSU16());
      nargs = 1;
      goto execute_fn;
    case op_execute_global2:
      called = GVAR(globals, INSU16());
      nargs = 2;
      goto execute_fn;
    case op_execute:
      called = FAST_POP();
      nargs = INSU8();

    execute_fn:
      {
	int err;

#if 0
	MDBG8(dbg_exec); MDBG8(nargs); MDBG16(called);
#endif
	SAVE_SP(); SAVE_INS();
	setup_call_stack(called, nargs);
	return;
      }

    case op_discard:
      FAST_POPN(1);
      break;
    case op_exit_n:
      {
	u8 i = INSU8();
	result = FAST_POP();
	FAST_POPN(i);
	FAST_PUSH(result);
	break;
      }
    case op_branch_z1:
      if (!istrue(FAST_POP())) goto branch1;
      (void)INSI8();
      break;
    case op_branch_z2:
      if (!istrue(FAST_POP())) goto branch2;
      (void)INSI16();
      break;
    case op_branch_nz1:
      if (istrue(FAST_POP())) goto branch1;
      (void)INSI8();
      break;
    case op_branch_nz2:
      if (istrue(FAST_POP())) goto branch2;
      (void)INSI16();
      break;

    {
      int16_t offset;

    case op_loop2: 
      offset = INSI16();
      goto loop;

    case op_loop1:
      offset = INSI8();
    loop:
      FAST_POPN(1);
      if (!--context.call_count) 
	runtime_error(error_loop);

      /* We exit the interpreter on backwards branches to ensure that
	 external conditions are noticed */
      INSSKIP(offset);
      SAVE_SP(); SAVE_INS();
      return;
    }

    case op_branch1:
    branch1:
    {
      i8 offset = INSI8();
      INSSKIP(offset);
      break;
    }

    case op_branch2:
    branch2:
    {
      i16 offset = INSI16();
      INSSKIP(offset);
      break;
    }

#define LOCAL frame->locals[frame->fn->code->nb_locals - 1 - INSU8()]
#define CLOSURE frame->fn->variables[INSU8()]

#define RECALL(access) FAST_RESERVE(1); FAST_PUSH(access->vvalue)
#define ASSIGN(access) { (access)->vvalue = FAST_GET(0); }

    case op_clear_local: ((struct variable *)LOCAL)->vvalue = NULL; break;
    case op_recall + local_var: RECALL(LOCAL); break;
    case op_recall + closure_var: RECALL(CLOSURE); break;
    case op_recall + global_var: FAST_RESERVE(1); FAST_PUSH(GVAR(globals, INSU16())); break;
    case op_assign + local_var: ASSIGN(LOCAL); break;
    case op_assign + closure_var: ASSIGN(CLOSURE); break;
    case op_assign + global_var: 
#ifndef TINY
      {
	u16 goffset = INSU16();

	if (GCONSTANT(globals, goffset))
	  runtime_error(error_variable_read_only);
	else
	  GVAR(globals, goffset) = FAST_GET(0);
	break;
      }
#endif
    case op_define:		/* Like op_assign global, but no error checking */
      GVAR(globals, INSU16()) = FAST_GET(0);
      break;

      /* The builtin operations */
    case op_builtin_eq:
      arg1 = FAST_POP();
      FAST_SET(0, makebool(FAST_GET(0) == arg1));
      break;
    case op_builtin_neq:
      arg1 = FAST_POP();
      FAST_SET(0, makebool(FAST_GET(0) != arg1));
      break;

    case op_builtin_add:
      arg2 = FAST_POP();
      arg1 = FAST_GET(0);
      if (INTEGERP(arg1) && INTEGERP(arg2))
	FAST_SET(0, (value)((ivalue)arg1 + (ivalue)arg2 - 1));
#ifndef TINY
      else if (TYPE(arg1, type_string) && TYPE(arg2, type_string))
	{
	  SAVE_SP(); SAVE_INS();
	  arg1 = string_append(arg1, arg2);
	  RESTORE_INS();
	  FAST_SET(0, arg1);
	}
#endif
      else 
	runtime_error(error_bad_type);
      break;

    case op_builtin_negate:
      arg1 = FAST_GET(0);
      if (!INTEGERP(arg1))
	runtime_error(error_bad_type);
      FAST_SET(0, (value)(2 - (ivalue)arg1));
      break;

    case op_builtin_bitnot:
      arg1 = FAST_GET(0);
      if (!INTEGERP(arg1))
	runtime_error(error_bad_type);
      FAST_SET(0, (value)(~(uvalue)arg1 | 1));
      break;
    case op_builtin_not:
      FAST_SET(0, makebool(!istrue(FAST_GET(0))));
      break;

    case op_builtin_and:
      arg2 = FAST_POP();
      arg1 = FAST_GET(0);
      FAST_SET(0, makebool(istrue(arg1) && istrue(arg2)));
      break;
    case op_builtin_or:
      arg2 = FAST_POP();
      arg1 = FAST_GET(0);
      FAST_SET(0, makebool(istrue(arg1) || istrue(arg2)));
      break;

      /* These could be optimised */
    case op_builtin_ref:
      arg2 = FAST_POP();
#ifndef TINY
      SAVE_SP(); SAVE_INS();
#endif
      arg1 = code_ref(FAST_GET(0), arg2);
      if (arg1 == PRIMITIVE_STOLE_CC)
	arg1 = NULL;
      GCCHECK(arg1);
#ifndef TINY
      RESTORE_INS();
#endif
      FAST_SET(0, arg1);
      break;

    case op_builtin_set:
      arg2 = FAST_POP();
      arg1 = FAST_POP();
#ifndef TINY
      SAVE_SP(); SAVE_INS();
#endif
      arg1 = code_set(FAST_GET(0), arg1, arg2);
      if (arg1 == PRIMITIVE_STOLE_CC)
	arg1 = NULL;
      GCCHECK(arg1);
#ifndef TINY
      RESTORE_INS();
#endif
      FAST_SET(0, arg1);
      break;

      /* The type checks */
    case op_typecheck + type_integer:
      arg1 = LOCAL->vvalue;
      if (!INTEGERP(arg1))
	runtime_error(error_bad_type);
      break;

    case op_typecheck + type_string:
    case op_typecheck + type_vector:
    case op_typecheck + type_pair:
    case op_typecheck + type_symbol:
    case op_typecheck + type_table:
    case op_typecheck + type_function:
    case op_typecheck + type_null:
      arg1 = LOCAL->vvalue;
      if (!TYPE(arg1, byteop - op_typecheck))
	runtime_error(error_bad_type);
      break;

    case op_typecheck + stype_none:
      INSU8();
      runtime_error(error_bad_type);

    case op_typecheck + stype_list:
      arg1 = LOCAL->vvalue;
      if (!TYPE(arg1, type_null) && !TYPE(arg1, type_pair))
	runtime_error(error_bad_type);
      break;

    default: assert(0);
    }
  goto nextins;
  /*SAVE_SP(); SAVE_INS(); */
}

static CC interpret_action2(frameact action, u8 **ffp, u8 **fsp)
{
  u8 *lfp = *ffp;
  struct interpret_frame *frame = (struct interpret_frame *)lfp;
  /* Note: this is safe because while frame->fn may have been forwarded
     already, the original object is still present unchanged (except for
     the forwarding info) at it's original location. We do want to
     read this before the forwarding of frame->fn, though. */
  u8 nb_locals = frame->fn->code->nb_locals;

  switch (action)
    {
    case fa_print:
#ifndef TINY
      print_bytecode(frame);
#endif
      break;
    case fa_gcforward: 
      {
	value *values, *last;

	forward((value *)&frame->fn);

	/* Forward stack */
	values = (value *)*fsp;
	last = (value *)lfp;
	while (values < last)
	  forward(values++);
	
	/* Forward locals */
	values = (value *)frame->locals;
	last = values + nb_locals;
	while (values < last)
	  forward(values++);
      }
      /* fall through */
    case fa_unwind: case fa_pop:
      pop_frame(ffp, fsp, offsetof(struct interpret_frame, locals) +
		nb_locals * sizeof(value));
      MDBG8(0xe5); MDBG16(sp);
      break;
    default: abort();
    }
}

static CC interpret_action(frameact action, u8 **ffp, u8 **fsp)
{
  if (action == fa_execute)
    execute_bytecode((struct interpret_frame *)*ffp);
  else
    interpret_action2(action, ffp, fsp);
}


CC push_closure(struct closure *fn, u8 nargs)
{
  struct code *code = fn->code;
  struct interpret_frame *frame;
  u8 nb_nonargs = code->nb_locals - code->nargs;
  int frame_size = offsetof(struct interpret_frame, locals) +
    nb_nonargs * sizeof(value);
  u16 i;
  i8 fnargs = code->nargs;
  value *args = (value *)sp;
  struct vector *vargs;
  bool ok;

  MDBG8(dbg_push_closure);
  MDBG16(code);
  MDBG16(sp);

  if (fnargs < 0)
    {
      /* varargs */
      u8 j;

      vargs = (struct vector *)unsafe_allocate_record(type_vector, nargs);

      for (j = 0; j < nargs; j++)
	vargs->data[nargs - j - 1] = args[j];

      /* Pop args from stack and reserve space for vargs */
      sp += nargs * sizeof(value);
      frame_size += sizeof(value);
    }

  GCPRO1(fn);
  frame = push_frame(interpret_action, frame_size);
  GCPOP(1);

  code = fn->code;
  frame->ins = 0;
  frame->fn = fn;
  for (i = 0; i < nb_nonargs; i++)
    frame->locals[i] = NULL;

#ifndef TINY
  /* Profiling */
  code->call_count++;
  frame->start_ins = instruction_number;
#endif

  if (fnargs < 0)
    /* Save vargs in the right place */
    frame->locals[code->nb_locals - 1] = (value)vargs;
  else
    {
      /* fixed args, already in place, just check number */
      if (nargs != (u8)fnargs)
	{
#ifndef TINY
	  if (context.display_error)
	    actual_args = nargs; /* This is an evil hack */
#endif
	  runtime_error(error_wrong_parameters);
	}
    }

  /* Make local variables */
  allocate_locals(frame->locals, code->nb_locals);

  /* We don't check for infinite loops through function calls because
     these will run out of memory anyway */
}

#if 0
/* This is what push_closure looks like for non-varargs functions, and
   assuming we don't need the GCPRO1 around push_frame and the 
   allocate_locals */
CC testing(struct closure *fn, u8 nargs)
{
  struct interpret_frame *frame;
  u8 nb_nonargs = fn->code->nb_locals - fn->code->nargs;
  int frame_size = offsetof(struct interpret_frame, locals) +
    nb_nonargs * sizeof(value);
  u16 i;

  frame = push_frame(interpret_action, frame_size);
  frame->ins = 0;
  frame->fn = fn;
  for (i = 0; i < nb_nonargs; i++)
    frame->locals[i] = NULL;

  if (nargs != (u8)fn->code->nargs)
    runtime_error(error_wrong_parameters);
}
#endif
