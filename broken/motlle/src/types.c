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
#include <ctype.h>
#include <stdlib.h>
#include <errno.h>
#include <math.h>
#include "mudlle.h"
#include "types.h"
#include "alloc.h"

struct closure *unsafe_alloc_and_push_closure(u8 nb_variables)
{
  /* This could (should?) be optimised to avoid the need for
     GCPRO1/stack_reserve/GCPOP */
  struct closure *newp = (struct closure *)unsafe_allocate_record(type_function, nb_variables + 1);

  SET_READONLY(newp);
  GCPRO1(newp);
  stack_reserve(sizeof(value));
  GCPOP(1);
  stack_push(newp);

  return newp;
}

#ifndef STANDALONE
struct closure *alloc_closure0(struct code *code)
{
  struct closure *newp;

  GCCHECK(code);
  GCPRO1(code);
  newp = (struct closure *)allocate_record(type_function, 1);
  GCPOP(1);
  newp->code = code;
  SET_READONLY(newp);

  return newp;
}
#endif

#ifndef TINY
struct string *alloc_string(const char *s)
{
  struct string *newp;

  newp = (struct string *)allocate_string(type_string, strlen(s) + 1);

  strcpy(newp->str, s);

  return newp;
}

struct symbol *alloc_symbol(struct string *name, value data)
{
  struct symbol *newp;

  GCCHECK(name);
  GCCHECK(data);
  GCPRO2(name, data);
  newp = (struct symbol *)unsafe_allocate_record(type_symbol, 2);
  GCPOP(2);
  newp->name = name;
  newp->data = data;

  return newp;
}

struct extptr *alloc_extptr(void *ext)
{
  struct extptr *t = (struct extptr *)allocate_string(type_null, sizeof(struct extptr));
  t->external = ext;
  return t;
}
#endif

struct vector *alloc_vector(uvalue size)
{
  return (struct vector *)allocate_record(type_vector, size);
}

struct string *alloc_string_n(uvalue n)
{
  struct string *newp;

  newp = (struct string *)allocate_string(type_string, n + 1);
  newp->str[n] = '\0';

  return newp;
}

struct list *alloc_list(value car, value cdr)
{
  struct list *newp;

  GCCHECK(car);
  GCCHECK(cdr);
  GCPRO2(car, cdr);
  newp = (struct list *)unsafe_allocate_record(type_pair, 2);
  GCPOP(2);
  newp->car = car;
  newp->cdr = cdr;

  return newp;
}

