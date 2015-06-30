import net.tinyos.script.*;
import java.io.*;
import java.util.*;

public class GenPrims {
    
    public static void main(String[] args) {
	new GenPrims(args);
    }

    GenPrims(String[] args) {
	try {
	    Primitive pgen = (Primitive)Class.forName(args[0]).newInstance();
	    DFTokenizer t = new DFTokenizer(new FileReader(args[1]));
	    int prim = 0;

	    pgen.header();
	    while (t.hasMoreStatements()) {
		DFStatement c = t.nextStatement();

		if (c == null)
		    break;
		String type = c.getType();

		if (type.equals("FUNCTION")) {
		    String name = c.get("username");
		    String opcode = c.get("opcode");
		    String component = c.get("component");

		    if (name == null) // a mate function
			name = c.get("name");
		    if (component == null) // default is opcode name
			component = opcode;

		    pgen.primitive(prim++,
				   name, opcode, component,
				   c.get("desc"),
				   atoi(c.get("numparams")),
				   atobool(c.get("returnval")));
		}
	    }
	    pgen.footer();
	}
	catch (Exception e) {
	    System.out.println("oops " + e);
	}
    }

    static void p(String s) { System.out.print(s); }

    static int atoi(String s) {
	try {
	    return Integer.parseInt(s);
	}
	catch (NumberFormatException e) {
	    return 0;
	}
    }
    
    static boolean atobool(String s) {
	if (s.equalsIgnoreCase("false"))
	    return false;
	return true;
    }
}

abstract class Primitive {
    static void p(String s) { System.out.print(s); }

    void header() { }
    abstract void primitive(int index, String name, String opcode,
			    String component, String desc,
			    int numParams, boolean returnVal);
    void footer() { }
}

class vmargs extends Primitive {
    void header() {
	p("prog_char args[] = {\n");
    }
	
    void primitive(int index, String name, String opcode, String component,
		   String desc, int numParams, boolean returnVal) {
	if (index > 0) p(", ");
	p("" + numParams);
    }

    void footer() {
	p("\n};\n");
    }
}

class vmret extends Primitive {
    void header() {
	p("prog_char retval[] = {\n");
    }
	
    void primitive(int index, String name, String opcode, String component,
		   String desc, int numParams, boolean returnVal) {
	if (index > 0) p(", ");
	p("" + (returnVal ? 1 : 0));
    }

    void footer() {
	p("\n};\n");
    }
}

class vmdispatch extends Primitive {
    void header() {
	p("configuration MotllePrimitives {\n");
	p("  provides interface MateBytecode as Primitives[uint16_t id];\n");
	p("} implementation {\n");
	p("  components ");
    }

    Vector v = new Vector();
	
    void primitive(int index, String name, String opcode, String component,
		   String desc, int numParams, boolean returnVal) {
	if (index > 0) p(", ");
	p("OP" + component);
	v.add(component);
    }

    void footer() {
	p(";\n\n");
	int s = v.size();

	for (int i = 0; i < s; i++) {
	    p("  Primitives["+ i + "] = OP" + v.elementAt(i) + ";\n");
	}
	p("}\n");
    }
}

class compilertable extends Primitive {
    void header() {
	p("struct primitive_ext mprimitives[] = {\n");
    }
	
    int count;
	
    void primitive(int index, String name, String opcode, String component,
		   String desc, int numParams, boolean returnVal) {
	if (index > 0) p(",\n");
	p("  { \"" + name + "\", \"" + desc + "\",\n" +
	  "    NULL, " + numParams + ", 0, NULL }");
	count++;
    }

    void footer() {
	p("\n};\n");

	p("struct primitive_ext *mprimops[] = {\n");
	for (int i = 0; i < count; i++) {
	    p("  &mprimitives[" + i + "]");
	    if (i < count - 1) p(",\n");
	}
	p("\n};\n");
    }
}
