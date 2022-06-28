package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.SingleFormParam;
import rs.ac.bg.etf.pp1.ast.VarIdentDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FnParamCounter extends CounterVisitor{
	
		public void visit(SingleFormParam param){
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VarIdentDecl varDecl){
			count++;
		}
	}
}
