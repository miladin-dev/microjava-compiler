// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class SingleExpr extends Expr {

    private OptMinus OptMinus;
    private Term Term;

    public SingleExpr (OptMinus OptMinus, Term Term) {
        this.OptMinus=OptMinus;
        if(OptMinus!=null) OptMinus.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public OptMinus getOptMinus() {
        return OptMinus;
    }

    public void setOptMinus(OptMinus OptMinus) {
        this.OptMinus=OptMinus;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptMinus!=null) OptMinus.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptMinus!=null) OptMinus.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptMinus!=null) OptMinus.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleExpr(\n");

        if(OptMinus!=null)
            buffer.append(OptMinus.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleExpr]");
        return buffer.toString();
    }
}
