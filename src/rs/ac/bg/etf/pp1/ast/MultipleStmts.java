// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class MultipleStmts extends MultipleStatements {

    private MultipleStatements MultipleStatements;
    private Statement Statement;

    public MultipleStmts (MultipleStatements MultipleStatements, Statement Statement) {
        this.MultipleStatements=MultipleStatements;
        if(MultipleStatements!=null) MultipleStatements.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public MultipleStatements getMultipleStatements() {
        return MultipleStatements;
    }

    public void setMultipleStatements(MultipleStatements MultipleStatements) {
        this.MultipleStatements=MultipleStatements;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MultipleStatements!=null) MultipleStatements.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MultipleStatements!=null) MultipleStatements.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MultipleStatements!=null) MultipleStatements.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleStmts(\n");

        if(MultipleStatements!=null)
            buffer.append(MultipleStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleStmts]");
        return buffer.toString();
    }
}
