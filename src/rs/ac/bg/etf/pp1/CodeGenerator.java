package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	Logger log = Logger.getLogger(getClass());
	
	private boolean checkType(Struct str1, Struct str)
	{	
		if (str1.equals(str))
			return true;
		else
			return false;
	}
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}

	public void visit(MethodName meth)
	{
		String meth_name = meth.getMethName();
		if(meth_name.equals("main")) {
			mainPc = Code.pc;
		}
		
		meth.obj.setAdr(Code.pc); 
//		Obj meth_obj = TabSym.find(meth_name);
//		meth_obj.setAdr(Code.pc);
		
		SyntaxNode node = meth.getParent();
		
		CounterVisitor.FnParamCounter fn = new CounterVisitor.FnParamCounter();
		CounterVisitor.VarCounter var = new CounterVisitor.VarCounter();
		
		node.traverseTopDown(fn);
		node.traverseTopDown(var);
		
		
		Code.put(Code.enter);
		Code.put(fn.getCount());
		Code.put(fn.getCount() + var.getCount());
	
	}
	
	public void visit(MethodDecl meth)
	{
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	/**			Expr			**/
	public void visit(MultipleExpr expr)
	{
		int temp = addop_list.remove(addop_list.size() - 1);
		switch(temp) {
			case PLUS:
				Code.put(Code.add);
				break;
			case MINUS:
				Code.put(Code.sub);
				break;
			case MUL:
				Code.put(Code.mul);
				break;
		}
	}
	
	public void visit(MultipleTerm term)
	{
		int temp = mulop_list.remove(mulop_list.size() - 1);
		switch(temp) {
		case MUL:
			Code.put(Code.mul);
			break;
		case DIV:
			Code.put(Code.div);
			break;
		case MOD:
			Code.put(Code.rem);
			break;
		}
	}

	public void visit(SingleExpr expr)
	{
		if (expr.getOptMinus() instanceof OptMin)
		{
			Code.put(Code.neg);
		}
	}
	
	/**			Factor			**/
	

	//Factor = NUMCONST
	public void visit(FactorNum factorNumber)
	{
		int val = factorNumber.getN1();
		Code.loadConst(val);
	}
	
	//Factor = CHARCONST
	public void visit(FactorChar factorChar)
	{
		int val = (int)factorChar.getC1().charAt(1);
		Code.loadConst(val);
	}
	
	public void visit(FactorBool factorBool)
	{
		int val = (factorBool.getB1() ? 1 : 0);
		Code.loadConst(val);
	}
	
	//Factor = Designator(Act);
	public void visit(FactorFnAct fact)
	{
		Obj meth_obj = fact.getDesignator().obj;
		if (meth_obj.getName().equals("ord") || meth_obj.getName().equals("chr"))
		{
			return;
		}
		int off = meth_obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(off);
	}
	
	//Factor = Designator();
	public void visit(FactorFn fact)
	{
		Obj meth_obj = fact.getDesignator().obj;
		int off = meth_obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(off);
		
		//log.info(meth_obj.getName() + " " + meth_obj.getAdr() +" PC =  " + Code.pc + "OFF = "+ off);
	}
	
	//Factor = new Type [Expr]
	public void visit(FactorNewExpr factArr)
	{
		Code.put(Code.newarray);
		if (factArr.getType().struct == TabSym.charType)
		{
			Code.put(0);
		}
		else
		{
			Code.put(1);
		}
	}
	
	public void visit(FactorNew fact)
	{
		Code.put(Code.new_);
		Code.put2(fact.getType().struct.getNumberOfFields() * 4);
	}
	
	/**			Designator			**/
	
	
	public void check_design(Designator des, Obj o)
	{
		SyntaxNode parent = des.getParent();
		
		//Ako nije usao ovde usled poziva ovog -> (Designator) = Expr;, ne celogo ovog stetmenta nego samo Designator
		if(parent.getClass() != DesStmtEq.class
				&& parent.getClass() != FactorFn.class
				&& parent.getClass() != FactorFnAct.class)
		{
			Code.load(o); //ucitaj na stek
		}		
		
	}
	
	//Designator = IDENT 
	public void visit(DelegDesign des)
	{
		check_design(des, des.obj);
	}
	
	//arr[0] = 5;
	//arr
	//const 0
	//5
	//Designator = IDENT '[' Expr ']'
	public void visit(DesignArrayIndex desArray)
	{
		SyntaxNode node = desArray.getParent();

		Code.load(desArray.obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		
		
		//Ako nije sa leve strane =;
		if (!(node instanceof DesStmtEq || node instanceof StmtRead))
		{
			if (desArray.obj.getType().getElemType() == TabSym.charType)
				Code.put(Code.baload);
			else
				Code.put(Code.aload);
		}
	}
	
	//Designator ::= (DesignDot) DesignIdent . IDENT:desTip
	public void visit(DesignDot des)
	{
		Obj classObj = des.getDesignIdent().obj;
		//log.info(classObj.getName());
		Code.load(classObj);
		
		SyntaxNode parent = des.getParent();
		
		//Ako nije sa leve strane jednakosti, stavi i getfield odmah na stek(tj member na stek);
		if(parent.getClass() != DesStmtEq.class)
		{
			Code.load(des.obj);
		}
	}
	
	
	//Designator ::=  (DesignDotArray) DesignIdent.IDENT[Expr]
	public void visit(DesignDotArray des)
	{
		/* ovako radi */
		//t1
		//const_5
		//
		
		//5
		//t1
		//arr
		
		
		/* ovako treba */
		//t1.arr[5];
		//t1 -> getstatic
		//arr -> getfield 
		//const_5
	
		//Da index bude prvo pa adr strukture
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		//Ovaj load ce sada na vrh steka imati heap[adr + fld_adr];
		Code.load(des.obj);
		
		//Samo zameni da bude polje na vrhu steka, a index ispod njega
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		//log.info(des.obj.getName());
		
		SyntaxNode node = des.getParent();
		if (!(node instanceof DesStmtEq || node instanceof StmtRead))
		{
			if (des.obj.getType().getElemType() == TabSym.charType)
				Code.put(Code.baload);
			else
				Code.put(Code.aload);
		}
	}
	
	public void visit(DesignIdent des)
	{
		//Ako je t1.arr[0], stavi t1 pa posle samo zameni 0 i arr sa dupx
		if (des.getParent() instanceof DesignDotArray)
		{
			Obj classObj = des.obj;
			Code.load(classObj);
		}
	}
	
	
	/**			DesignatorStatement 		**/
	
	//DesignatorStatement = Designator Assignop Expr
	public void visit(DesStmtEq des)
	{
		if (des.getDesignator() instanceof DesignArrayIndex || des.getDesignator() instanceof DesignDotArray)
		{
			if (des.getDesignator().obj.getType().getElemType() == TabSym.charType)
				Code.put(Code.bastore);
			else
				Code.put(Code.astore);				
		}
		else
		{
			Code.store(des.getDesignator().obj);			
		}
	}
	
	//DesigntStmt = Designator++;
	public void visit(DesStmtPLL despp)
	{
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(despp.getDesignator().obj);
	}
	
	//DesigntStmt = Designator--;
	public void visit(DesStmtMINN despp)
	{
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(despp.getDesignator().obj);
	}
	
	/**			Statement			**/
	
	//SingleStatement = (StmtPrint) PRINT Expr;
	public void visit(StmtPrint stmt)
	{
		Struct expr_type = stmt.getExpr().struct;
		if(checkType(expr_type, TabSym.charType))
		{
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
		else
		{
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}

	//SingleStatement = (StmtRead) READ Designator;
	public void visit(StmtRead stmt)
	{
		Obj obj = stmt.getDesignator().obj;
		Code.put(Code.read);
		designator_store(stmt.getDesignator());
		
	}
	
	private void designator_store(Designator des)
	{
		
		if (des instanceof DesignArrayIndex || des instanceof DesignDotArray)
		{
			if (des.obj.getType().getElemType() == TabSym.charType)
				Code.put(Code.bastore);
			else
				Code.put(Code.astore);				
		}
		else
		{
			Code.store(des.obj);			
		}
	}
	

	
	//SingleStatement = (StmtBreak) BREAK;
	public void visit(StmtBreak stmt)
	{
		Code.putJump(0);
		curr_break_fix.add(Code.pc - 2);
		//log.info(curr_break_fix.size());
	}
	
	public void visit(StmtContinue stmt)
	{
		Code.putJump(0);
		curr_continue.add(Code.pc - 2);
	}
	
	/**			Statement DO_WHILE			**/
	private List<Integer> curr_break_fix = null;
	private List<List<Integer>> breaksList = new ArrayList<>();
	private List<Integer> curr_continue = null;
	private List<List<Integer>> continueList = new ArrayList<>();
	private List<Integer> dowhile_tofix = null;
	private List<List<Integer>> dowhile_list = new ArrayList<>();
	private Stack<Integer> loops_called = new Stack<>();
	private Stack<Integer> do_while_pc = new Stack<>();
	private static final int WHILE = 1;
	private static final int IFELSE = 0;
	
	private int DO_PC;
	
	public void visit(KeywordDo kwdo)
	{
		loops_called.add(WHILE);
		
		//List for brakes; 
		curr_break_fix = new ArrayList<>();
		breaksList.add(curr_break_fix);
		
		//List for continues;
		curr_continue = new ArrayList<>();
		continueList.add(curr_continue);
		
		//Storing PC when DO keyword starts.
		DO_PC = Code.pc;
		do_while_pc.add(DO_PC);
		
		if_tofix = new ArrayList<>();
		if_list.add(if_tofix);
	}
	
	
	//SingleStatement = (StmtDoWhile) DO { Statement } WHILE(Condition);
	public void visit(StmtDoWhile dowhile)
	{
		loops_called.pop();
		fixup_dowhile();
		
		//Update outer break statements to fix;
		breaksList.remove(breaksList.size() - 1);
		if (!breaksList.isEmpty())
			curr_break_fix = breaksList.get(breaksList.size() - 1);
		
		//Update outer continue statements to fix;
		continueList.remove(continueList.size() - 1);
		if(!continueList.isEmpty())
			curr_continue = continueList.get(continueList.size() - 1);
	}
	
	//KeywordWhile ::= (KeywordWhile) KEYWORD_WHILE;
	public void visit(KeywordWhile stmt)
	{
		//Fix continue statements to jump to this addres before condition check.
		while(!curr_continue.isEmpty())
		{
			int fixAddr = curr_continue.remove(curr_continue.size() - 1);
			Code.fixup(fixAddr);
		}
	}
	
	
	/**			Statement IF			**/


	private List<Integer> if_tofix = null;
	private List<List<Integer>> if_list = new ArrayList<>();
	private List<Integer> curr_jump_overElse = null;
	private List<List<Integer>> listJumpoverElse = new ArrayList<>();
	
	//SingleStatement = (StmIfNoBrace) IF (ConditionIf) Statement
	public void visit (StmIfNoBrace ifstmt)
	{
		fixup_cond();
		
		
		//Uzimanje okruzujuce if/dowhile petlje;
		if_list.remove(if_list.size() - 1);
		if (if_list.size() > 0)
			if_tofix = if_list.get(if_list.size() - 1);
		
		loops_called.pop();
		
		listJumpoverElse.remove(listJumpoverElse.size() - 1);
		if(!listJumpoverElse.isEmpty())
			curr_jump_overElse = listJumpoverElse.get(listJumpoverElse.size() - 1);
	}
	
	public void visit (KeywordElse els)
	{
		//Za izlazak iz if tela i da se preskoci else.
		Code.putJump(0);
		curr_jump_overElse.add(Code.pc - 2);
		
		fixup_cond();
		if_list.remove(if_list.size() - 1);
		
		if (if_list.size() > 0)
			if_tofix = if_list.get(if_list.size() - 1);
		
	}
	
	public void visit (KeywordIf kwif)
	{
		loops_called.add(IFELSE);
		if_tofix = new ArrayList<>();
		if_list.add(if_tofix);
		
		curr_jump_overElse = new ArrayList<>();
		listJumpoverElse.add(curr_jump_overElse);
	}
	
	public void visit(MtchIfStmtBrackets stmt)
	{
		int skipOverElse = curr_jump_overElse.get(0);
		Code.fixup(skipOverElse);

		loops_called.pop();
		
		listJumpoverElse.remove(listJumpoverElse.size() - 1);
		if(!listJumpoverElse.isEmpty())
			curr_jump_overElse = listJumpoverElse.get(listJumpoverElse.size() - 1);
	}
	
	
	/**			Condition			**/
	
	
	public static final int EQEQ = 0;
	public static final int NE = 1;
	public static final int GR = 4;
	public static final int GRE = 5;
	public static final int LS = 2;
	public static final int LSE = 3;
	private List<Integer> relop_list = new ArrayList<>();
	private int visited_relop = -1;
	
	//if (x == 1 || x == 0 || x == 2)
	
	//MultipleCond [((MultipleCond) ((singlecond) CondTerm)* OROR CondTerm)* OROR CondTerm]
	//MultipleCond [((MultipleCond) ((singlecond)  [{MultipleCondTerm} ((singlecondTerm)CondFact) ANDAND CondFact] )* OROR CondTerm)* OROR CondTerm]
	
	//if (x == 1 && y == 1)
	// SingleCond [MultipleCondTerm {(singleCondTerm-(CondFact)) ANDAND CondFact}]* -> skacu nakon singlecond, ali to je vec obezbedjeno nakon ifa i elsa 
	private void putRightJump(int op, int adr)
	{
		Code.put(Code.jcc + op); 
		Code.put2(adr - Code.pc + 1);
	}
	
	
	public void visit(CondExpr cond)
	{
		//if(x)
		//x
		//0
		//jeq skaci izvan petlje
		if (loops_called.peek() == IFELSE)
		{
			Code.loadConst(0);
			Code.putFalseJump(NE, 0);
			
			if_tofix.add(Code.pc - 2);
		}
		else 
		{
			Code.loadConst(0);
			putRightJump(NE, 0);
			
			if_tofix.add(Code.pc - 2);
		}
			
	}
	
	public void visit(CondRelOp cond)
	{
		int val = relop_list.remove(relop_list.size() - 1);
		
		switch(val) 
		{
			case EQEQ:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(EQEQ, 0);
				else
					putRightJump(EQEQ, 0);
				
				if_tofix.add(Code.pc - 2);
				break;
			case NE:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(NE, 0);
				else
					putRightJump(NE, 0);
				
				if_tofix.add(Code.pc - 2);
				break;
			case LS:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(LS, 0);
				else
					putRightJump(LS, 0);
				if_tofix.add(Code.pc - 2);
				break;
			case LSE:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(LSE, 0);
				else
					putRightJump(LSE, 0);
				
				if_tofix.add(Code.pc - 2);
				break;
			case GR:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(GR, 0);
				else
					putRightJump(GR, 0);
				
				if_tofix.add(Code.pc - 2);
				break;
			case GRE:
				if (loops_called.peek() == IFELSE)
					Code.putFalseJump(GRE, 0);
				else
					putRightJump(GRE, 0);
				
				if_tofix.add(Code.pc - 2);
				break;
		}
	}
	
	public void visit(SingleCond cond)
	{
		if (cond.getParent() instanceof MultipleCond)
		{
			fixup_cond();
		}
	}
	public void visit(MultipleCond cond)
	{
		
		if (cond.getParent() instanceof MultipleCond)
		{
			fixup_cond();
		}
	}
	

	private void fixup_cond()
	{
		while (!if_tofix.isEmpty())
		{
			int fixPc = if_tofix.remove(if_tofix.size() - 1);
			Code.fixup(fixPc);
		}
	}
	
	private void fixup_dowhile()
	{
		DO_PC = do_while_pc.pop();
		
		while (!if_tofix.isEmpty())
		{
			int fixPc = if_tofix.remove(if_tofix.size() - 1);
			Code.put2(fixPc, DO_PC - (Code.pc - 2) + 1);
		}
		
		while(!curr_break_fix.isEmpty())
		{
			//log.info("CODE PC" + Code.pc);
			int tofix = curr_break_fix.remove(curr_break_fix.size() - 1);
			Code.fixup(tofix);
		}
	}
	

	
	/**			Operations			**/
	
	private List<Integer> mulop_list = new ArrayList<>();
	private List<Integer> addop_list = new ArrayList<>();
	private int last_addop = 0;
	private int last_mulop = 0;
	private static final int PLUS = 1;
	private static final int MINUS = -1;
	private static final int MUL = 2;
	private static final int DIV = 3;
	private static final int MOD = 4;
	
	public void visit(AddopPlus op)
	{
		last_addop = PLUS;
		addop_list.add(last_addop);
	}
	
	public void visit(AddopMinus op)
	{
		last_addop = MINUS;
		addop_list.add(last_addop);
	}
	
	public void visit(MulAsteriks mul)
	{
		last_mulop = MUL;
		mulop_list.add(last_mulop);
	}
	
	public void visit(MulDiv mul)
	{
		last_mulop = DIV;
		mulop_list.add(last_mulop);
	}
	
	public void visit(MulMod mul)
	{
		last_mulop = MOD;
		mulop_list.add(last_mulop);
	}

	public void visit(Equalss eqeq)
	{
		visited_relop = 0;
		relop_list.add(visited_relop);
	}
	
	public void visit(Different diff)
	{
		visited_relop = 1;
		relop_list.add(visited_relop);
	}
	
	public void visit(Greater gr)
	{
		visited_relop = 4;
		relop_list.add(visited_relop);
	}
	
	public void visit(GreaterEq gre)
	{
		visited_relop = 5;
		relop_list.add(visited_relop);
	}
	
	public void visit(Lesser ls)
	{
		visited_relop = 2;
		relop_list.add(visited_relop);
	}
	
	public void visit(LesserEq lse)
	{
		visited_relop = 3;
		relop_list.add(visited_relop);
	}
	
	
}
