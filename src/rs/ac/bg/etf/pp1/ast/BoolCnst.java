// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class BoolCnst extends LiteralConst {

    private BoolConst BoolConst;

    public BoolCnst (BoolConst BoolConst) {
        this.BoolConst=BoolConst;
        if(BoolConst!=null) BoolConst.setParent(this);
    }

    public BoolConst getBoolConst() {
        return BoolConst;
    }

    public void setBoolConst(BoolConst BoolConst) {
        this.BoolConst=BoolConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BoolConst!=null) BoolConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BoolConst!=null) BoolConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BoolConst!=null) BoolConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolCnst(\n");

        if(BoolConst!=null)
            buffer.append(BoolConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolCnst]");
        return buffer.toString();
    }
}
