// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class FormParameters extends FormParamList {

    private FormParamList FormParamList;
    private FormParameter FormParameter;

    public FormParameters (FormParamList FormParamList, FormParameter FormParameter) {
        this.FormParamList=FormParamList;
        if(FormParamList!=null) FormParamList.setParent(this);
        this.FormParameter=FormParameter;
        if(FormParameter!=null) FormParameter.setParent(this);
    }

    public FormParamList getFormParamList() {
        return FormParamList;
    }

    public void setFormParamList(FormParamList FormParamList) {
        this.FormParamList=FormParamList;
    }

    public FormParameter getFormParameter() {
        return FormParameter;
    }

    public void setFormParameter(FormParameter FormParameter) {
        this.FormParameter=FormParameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParamList!=null) FormParamList.accept(visitor);
        if(FormParameter!=null) FormParameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParamList!=null) FormParamList.traverseTopDown(visitor);
        if(FormParameter!=null) FormParameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParamList!=null) FormParamList.traverseBottomUp(visitor);
        if(FormParameter!=null) FormParameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParameters(\n");

        if(FormParamList!=null)
            buffer.append(FormParamList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParameter!=null)
            buffer.append(FormParameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParameters]");
        return buffer.toString();
    }
}
