// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class MatchedIfStmt extends SingleStatement {

    private MatchedIfStatement MatchedIfStatement;

    public MatchedIfStmt (MatchedIfStatement MatchedIfStatement) {
        this.MatchedIfStatement=MatchedIfStatement;
        if(MatchedIfStatement!=null) MatchedIfStatement.setParent(this);
    }

    public MatchedIfStatement getMatchedIfStatement() {
        return MatchedIfStatement;
    }

    public void setMatchedIfStatement(MatchedIfStatement MatchedIfStatement) {
        this.MatchedIfStatement=MatchedIfStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MatchedIfStatement!=null) MatchedIfStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MatchedIfStatement!=null) MatchedIfStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MatchedIfStatement!=null) MatchedIfStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedIfStmt(\n");

        if(MatchedIfStatement!=null)
            buffer.append(MatchedIfStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedIfStmt]");
        return buffer.toString();
    }
}
