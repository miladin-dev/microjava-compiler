// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class DelegDesign extends Designator {

    private String idName;

    public DelegDesign (String idName) {
        this.idName=idName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName=idName;
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
        buffer.append("DelegDesign(\n");

        buffer.append(" "+tab+idName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DelegDesign]");
        return buffer.toString();
    }
}
