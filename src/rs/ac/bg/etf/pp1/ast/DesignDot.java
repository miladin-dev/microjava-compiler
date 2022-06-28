// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class DesignDot extends Designator {

    private DesignIdent DesignIdent;
    private String desTip;

    public DesignDot (DesignIdent DesignIdent, String desTip) {
        this.DesignIdent=DesignIdent;
        if(DesignIdent!=null) DesignIdent.setParent(this);
        this.desTip=desTip;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignIdent!=null) DesignIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignIdent!=null) DesignIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignIdent!=null) DesignIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignDot(\n");

        if(DesignIdent!=null)
            buffer.append(DesignIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+desTip);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignDot]");
        return buffer.toString();
    }
}
