// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class MtchIfStmtBrackets extends MatchedIfStatement {

    private KeywordIf KeywordIf;
    private ConditionIf ConditionIf;
    private Statement Statement;
    private KeywordElse KeywordElse;
    private Statement Statement1;

    public MtchIfStmtBrackets (KeywordIf KeywordIf, ConditionIf ConditionIf, Statement Statement, KeywordElse KeywordElse, Statement Statement1) {
        this.KeywordIf=KeywordIf;
        if(KeywordIf!=null) KeywordIf.setParent(this);
        this.ConditionIf=ConditionIf;
        if(ConditionIf!=null) ConditionIf.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.KeywordElse=KeywordElse;
        if(KeywordElse!=null) KeywordElse.setParent(this);
        this.Statement1=Statement1;
        if(Statement1!=null) Statement1.setParent(this);
    }

    public KeywordIf getKeywordIf() {
        return KeywordIf;
    }

    public void setKeywordIf(KeywordIf KeywordIf) {
        this.KeywordIf=KeywordIf;
    }

    public ConditionIf getConditionIf() {
        return ConditionIf;
    }

    public void setConditionIf(ConditionIf ConditionIf) {
        this.ConditionIf=ConditionIf;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public KeywordElse getKeywordElse() {
        return KeywordElse;
    }

    public void setKeywordElse(KeywordElse KeywordElse) {
        this.KeywordElse=KeywordElse;
    }

    public Statement getStatement1() {
        return Statement1;
    }

    public void setStatement1(Statement Statement1) {
        this.Statement1=Statement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KeywordIf!=null) KeywordIf.accept(visitor);
        if(ConditionIf!=null) ConditionIf.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(KeywordElse!=null) KeywordElse.accept(visitor);
        if(Statement1!=null) Statement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KeywordIf!=null) KeywordIf.traverseTopDown(visitor);
        if(ConditionIf!=null) ConditionIf.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(KeywordElse!=null) KeywordElse.traverseTopDown(visitor);
        if(Statement1!=null) Statement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KeywordIf!=null) KeywordIf.traverseBottomUp(visitor);
        if(ConditionIf!=null) ConditionIf.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(KeywordElse!=null) KeywordElse.traverseBottomUp(visitor);
        if(Statement1!=null) Statement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MtchIfStmtBrackets(\n");

        if(KeywordIf!=null)
            buffer.append(KeywordIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionIf!=null)
            buffer.append(ConditionIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(KeywordElse!=null)
            buffer.append(KeywordElse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement1!=null)
            buffer.append(Statement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MtchIfStmtBrackets]");
        return buffer.toString();
    }
}
