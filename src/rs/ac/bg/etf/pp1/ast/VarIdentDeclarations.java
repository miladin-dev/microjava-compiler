// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class VarIdentDeclarations extends VarIdentDeclList {

    private VarIdentDeclList VarIdentDeclList;
    private VarIdentDecl VarIdentDecl;

    public VarIdentDeclarations (VarIdentDeclList VarIdentDeclList, VarIdentDecl VarIdentDecl) {
        this.VarIdentDeclList=VarIdentDeclList;
        if(VarIdentDeclList!=null) VarIdentDeclList.setParent(this);
        this.VarIdentDecl=VarIdentDecl;
        if(VarIdentDecl!=null) VarIdentDecl.setParent(this);
    }

    public VarIdentDeclList getVarIdentDeclList() {
        return VarIdentDeclList;
    }

    public void setVarIdentDeclList(VarIdentDeclList VarIdentDeclList) {
        this.VarIdentDeclList=VarIdentDeclList;
    }

    public VarIdentDecl getVarIdentDecl() {
        return VarIdentDecl;
    }

    public void setVarIdentDecl(VarIdentDecl VarIdentDecl) {
        this.VarIdentDecl=VarIdentDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarIdentDeclList!=null) VarIdentDeclList.accept(visitor);
        if(VarIdentDecl!=null) VarIdentDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarIdentDeclList!=null) VarIdentDeclList.traverseTopDown(visitor);
        if(VarIdentDecl!=null) VarIdentDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarIdentDeclList!=null) VarIdentDeclList.traverseBottomUp(visitor);
        if(VarIdentDecl!=null) VarIdentDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarIdentDeclarations(\n");

        if(VarIdentDeclList!=null)
            buffer.append(VarIdentDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarIdentDecl!=null)
            buffer.append(VarIdentDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarIdentDeclarations]");
        return buffer.toString();
    }
}
