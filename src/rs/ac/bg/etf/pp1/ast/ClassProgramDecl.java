// generated with ast extension for cup
// version 0.8
// 24/0/2022 23:43:45


package rs.ac.bg.etf.pp1.ast;

public class ClassProgramDecl extends ProgramDecl {

    private ProgramDecl ProgramDecl;
    private ClassDecl ClassDecl;

    public ClassProgramDecl (ProgramDecl ProgramDecl, ClassDecl ClassDecl) {
        this.ProgramDecl=ProgramDecl;
        if(ProgramDecl!=null) ProgramDecl.setParent(this);
        this.ClassDecl=ClassDecl;
        if(ClassDecl!=null) ClassDecl.setParent(this);
    }

    public ProgramDecl getProgramDecl() {
        return ProgramDecl;
    }

    public void setProgramDecl(ProgramDecl ProgramDecl) {
        this.ProgramDecl=ProgramDecl;
    }

    public ClassDecl getClassDecl() {
        return ClassDecl;
    }

    public void setClassDecl(ClassDecl ClassDecl) {
        this.ClassDecl=ClassDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgramDecl!=null) ProgramDecl.accept(visitor);
        if(ClassDecl!=null) ClassDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgramDecl!=null) ProgramDecl.traverseTopDown(visitor);
        if(ClassDecl!=null) ClassDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgramDecl!=null) ProgramDecl.traverseBottomUp(visitor);
        if(ClassDecl!=null) ClassDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassProgramDecl(\n");

        if(ProgramDecl!=null)
            buffer.append(ProgramDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassDecl!=null)
            buffer.append(ClassDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassProgramDecl]");
        return buffer.toString();
    }
}
