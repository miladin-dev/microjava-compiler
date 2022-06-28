// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class VarIdentDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String varName;
    private OptionalArrayOp OptionalArrayOp;

    public VarIdentDecl (String varName, OptionalArrayOp OptionalArrayOp) {
        this.varName=varName;
        this.OptionalArrayOp=OptionalArrayOp;
        if(OptionalArrayOp!=null) OptionalArrayOp.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public OptionalArrayOp getOptionalArrayOp() {
        return OptionalArrayOp;
    }

    public void setOptionalArrayOp(OptionalArrayOp OptionalArrayOp) {
        this.OptionalArrayOp=OptionalArrayOp;
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
        if(OptionalArrayOp!=null) OptionalArrayOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalArrayOp!=null) OptionalArrayOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalArrayOp!=null) OptionalArrayOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarIdentDecl(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(OptionalArrayOp!=null)
            buffer.append(OptionalArrayOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarIdentDecl]");
        return buffer.toString();
    }
}
