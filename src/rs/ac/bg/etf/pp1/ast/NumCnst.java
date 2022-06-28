// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class NumCnst extends LiteralConst {

    private NumberConst NumberConst;

    public NumCnst (NumberConst NumberConst) {
        this.NumberConst=NumberConst;
        if(NumberConst!=null) NumberConst.setParent(this);
    }

    public NumberConst getNumberConst() {
        return NumberConst;
    }

    public void setNumberConst(NumberConst NumberConst) {
        this.NumberConst=NumberConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NumberConst!=null) NumberConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NumberConst!=null) NumberConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NumberConst!=null) NumberConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NumCnst(\n");

        if(NumberConst!=null)
            buffer.append(NumberConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NumCnst]");
        return buffer.toString();
    }
}
