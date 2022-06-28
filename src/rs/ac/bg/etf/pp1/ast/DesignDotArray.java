// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class DesignDotArray extends Designator {

    private DesignIdent DesignIdent;
    private String desTip;
    private Expr Expr;

    public DesignDotArray (DesignIdent DesignIdent, String desTip, Expr Expr) {
        this.DesignIdent=DesignIdent;
        if(DesignIdent!=null) DesignIdent.setParent(this);
        this.desTip=desTip;
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignIdent getDesignIdent() {
        return DesignIdent;
    }

    public void setDesignIdent(DesignIdent DesignIdent) {
        this.DesignIdent=DesignIdent;
    }

    public String getDesTip() {
        return desTip;
    }

    public void setDesTip(String desTip) {
        this.desTip=desTip;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignIdent!=null) DesignIdent.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignIdent!=null) DesignIdent.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignIdent!=null) DesignIdent.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignDotArray(\n");

        if(DesignIdent!=null)
            buffer.append(DesignIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+desTip);
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignDotArray]");
        return buffer.toString();
    }
}
