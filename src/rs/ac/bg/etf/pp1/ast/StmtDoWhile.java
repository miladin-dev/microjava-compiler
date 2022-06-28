// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class StmtDoWhile extends SingleStatement {

    private KeywordDo KeywordDo;
    private Statement Statement;
    private KeywordWhile KeywordWhile;
    private Condition Condition;

    public StmtDoWhile (KeywordDo KeywordDo, Statement Statement, KeywordWhile KeywordWhile, Condition Condition) {
        this.KeywordDo=KeywordDo;
        if(KeywordDo!=null) KeywordDo.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.KeywordWhile=KeywordWhile;
        if(KeywordWhile!=null) KeywordWhile.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public KeywordDo getKeywordDo() {
        return KeywordDo;
    }

    public void setKeywordDo(KeywordDo KeywordDo) {
        this.KeywordDo=KeywordDo;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public KeywordWhile getKeywordWhile() {
        return KeywordWhile;
    }

    public void setKeywordWhile(KeywordWhile KeywordWhile) {
        this.KeywordWhile=KeywordWhile;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KeywordDo!=null) KeywordDo.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(KeywordWhile!=null) KeywordWhile.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KeywordDo!=null) KeywordDo.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(KeywordWhile!=null) KeywordWhile.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KeywordDo!=null) KeywordDo.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(KeywordWhile!=null) KeywordWhile.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtDoWhile(\n");

        if(KeywordDo!=null)
            buffer.append(KeywordDo.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(KeywordWhile!=null)
            buffer.append(KeywordWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtDoWhile]");
        return buffer.toString();
    }
}
