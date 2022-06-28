// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class ConstAssign implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String constName;
    private LiteralConst LiteralConst;

    public ConstAssign (String constName, LiteralConst LiteralConst) {
        this.constName=constName;
        this.LiteralConst=LiteralConst;
        if(LiteralConst!=null) LiteralConst.setParent(this);
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public LiteralConst getLiteralConst() {
        return LiteralConst;
    }

    public void setLiteralConst(LiteralConst LiteralConst) {
        this.LiteralConst=LiteralConst;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LiteralConst!=null) LiteralConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LiteralConst!=null) LiteralConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LiteralConst!=null) LiteralConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstAssign(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(LiteralConst!=null)
            buffer.append(LiteralConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstAssign]");
        return buffer.toString();
    }
}
