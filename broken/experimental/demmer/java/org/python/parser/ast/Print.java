// Autogenerated AST node
package org.python.parser.ast;
import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Print extends stmtType {
    public exprType dest;
    public exprType[] values;
    public boolean nl;

    public Print(exprType dest, exprType[] values, boolean nl) {
        this.dest = dest;
        this.values = values;
        this.nl = nl;
    }

    public Print(exprType dest, exprType[] values, boolean nl, SimpleNode
    parent) {
        this(dest, values, nl);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Print[");
        sb.append("dest=");
        sb.append(dumpThis(this.dest));
        sb.append(", ");
        sb.append("values=");
        sb.append(dumpThis(this.values));
        sb.append(", ");
        sb.append("nl=");
        sb.append(dumpThis(this.nl));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(12, ostream);
        pickleThis(this.dest, ostream);
        pickleThis(this.values, ostream);
        pickleThis(this.nl, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitPrint(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (dest != null)
            dest.accept(visitor);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null)
                    values[i].accept(visitor);
            }
        }
    }

}
