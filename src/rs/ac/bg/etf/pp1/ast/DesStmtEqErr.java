// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class DesStmtEqErr extends DesignatorStatement {

    public DesStmtEqErr () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesStmtEqErr(\n");

        buffer.append(tab);
        buffer.append(") [DesStmtEqErr]");
        return buffer.toString();
    }
}
