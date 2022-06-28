package rs.ac.bg.etf.pp1.util;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class RuleVisitor extends VisitorAdaptor {

	public int progCnt = 0;
	public void visit(Program program)
	{
		progCnt++;
	}
}
