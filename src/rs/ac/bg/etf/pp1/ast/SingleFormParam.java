// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class SingleFormParam extends FormParameter {

    private Type Type;
    private String paramName;
    private OptionalArrayOp OptionalArrayOp;

    public SingleFormParam (Type Type, String paramName, OptionalArrayOp OptionalArrayOp) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.paramName=paramName;
        this.OptionalArrayOp=OptionalArrayOp;
        if(OptionalArrayOp!=null) OptionalArrayOp.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName=paramName;
    }

    public OptionalArrayOp getOptionalArrayOp() {
        return OptionalArrayOp;
    }

    public void setOptionalArrayOp(OptionalArrayOp OptionalArrayOp) {
        this.OptionalArrayOp=OptionalArrayOp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptionalArrayOp!=null) OptionalArrayOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalArrayOp!=null) OptionalArrayOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalArrayOp!=null) OptionalArrayOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleFormParam(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+paramName);
        buffer.append("\n");

        if(OptionalArrayOp!=null)
            buffer.append(OptionalArrayOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleFormParam]");
        return buffer.toString();
    }
}
