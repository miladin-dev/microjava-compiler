package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.*;

import java_cup.internal_error;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	
	public boolean errorDetected = false;
	private Struct assignedType = null;
	private Struct currentType = null;
	private Obj currentMethod = null;
	private int nVars;
	
	
	
	public int getnVars() {
		return nVars;
	}

	static
	{
		TabSym.insert(Obj.Type, "bool", TabSym.boolType);
		
	}
	
	public void report_error(String message, SyntaxNode info)
	{
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		
		if (line != 0)
		{
			msg.append(" - line ").append(line);
		}
		
		log.error(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info)
	{
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		
		if (line != 0)
		{
			msg.append(" - line ").append(line);
		}
		
		log.info(msg.toString());
	}
	
	boolean exists(String name)
	{
		Obj obj_ret = TabSym.find(name);
		
		if(obj_ret == TabSym.noObj)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	boolean exists_currScope(String name)
	{
		Obj obj_ret = TabSym.currentScope.findSymbol(name);
		
		if(obj_ret == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}


	/**			 Visit Methods 			**/
	
	
	public void visit(ProgName progName) 
	{
		String p_name = progName.getProgName();
		progName.obj = TabSym.insert(Obj.Prog, p_name, TabSym.noType);
		//report_info("Program '" + p_name + "' added to symbol table!", progName);
		TabSym.openScope();
	}
	
	public void visit(Program program) 
	{

		Obj mainn = TabSym.find("main");
		
		
		if (mainn == TabSym.noObj || mainn.getKind() != Obj.Meth)
		{
			report_error("Main method does not exist...", null);
		}
		
		nVars = TabSym.currentScope.getnVars();
		TabSym.chainLocalSymbols(program.getProgName().obj);
		TabSym.closeScope();
		
	}
	
	
	public void visit(Type type)
	{
		Obj ret_obj = TabSym.find(type.getTypeName());
		
		if(ret_obj == TabSym.noObj)
		{
			report_error("Type '"  + type.getTypeName() + "' not found  in symbol table.", null);
		}
		else
		{
			if (ret_obj.getKind() == Obj.Type)
			{
				type.struct = ret_obj.getType();
				currentType = ret_obj.getType();
			}
			else
			{
				report_error("'" + type.getTypeName() + "' is not a type!", type);
				type.struct = Tab.noType;
			}
		}
	}
	
	private List<Obj> local_var_list = new ArrayList<>();
	private List<Obj> global_var_list = new ArrayList<>();
	private List<Obj> class_var_list = new ArrayList<>();
	
	private void edit_variable_list(Obj o)
	{
		// Local variable
		if (currentMethod != null)
		{
			local_var_list.add(o);
		}
		else if (current_class == null)
		{
			global_var_list.add(o);
		}
		else if (current_class != null)
		{
			class_var_list.add(o);
		}
		
	}
	
	public void visit(VarIdentDecl varIdent)
	{
		if(exists_currScope(varIdent.getVarName()))
		{
			report_error("Symbol '" + varIdent.getVarName() + "' already exists in current scope!", varIdent);
		}
		else
		{
			if (varIdent.getOptionalArrayOp() instanceof ArrOp)
			{
				int kind = (current_record == null ? Obj.Var : Obj.Fld);
				Obj var_obj = TabSym.insert(kind, varIdent.getVarName(), new Struct(Struct.Array, currentType));
				edit_variable_list(var_obj);
			}
			else
			{
				int kind = (current_record == null ? Obj.Var : Obj.Fld);
				Obj var_obj = TabSym.insert(kind, varIdent.getVarName(), currentType);	
				edit_variable_list(var_obj);
			}
		}

	}
	
	
	/** 			Constants 			**/
	
	
	public int const_value = -1;
	public void visit(NumberConst numConst)
	{
		numConst.struct = TabSym.intType;
		assignedType = TabSym.intType;
		const_value = numConst.getNum();
	}
	
	public void visit(CharConst charConst)
	{
		charConst.struct = TabSym.charType;
		assignedType = TabSym.charType;
		//'a'
		//' = 0
		//a = 1;
		//' = 2
		const_value = (int) charConst.getCh().charAt(1);
	}
	
	public void visit(BoolConst boolConst)
	{
		boolConst.struct = TabSym.boolType;
		assignedType = TabSym.boolType;
		const_value = (boolConst.getBol() == true ? 1 : 0);
	}
	
	public void visit(ConstAssign constAssign)
	{
		if(assignedType.assignableTo(currentType))
		{
			String const_id = constAssign.getConstName();
			Obj constant_obj = TabSym.insert(Obj.Con, const_id, currentType);
			constant_obj.setAdr(const_value);
			edit_variable_list(constant_obj);
			
			//report_info("Constant '" + const_id + "' added to symbol table!", constAssign);
		}
		else
		{
			report_error("Types do not correspond...", constAssign);
		}
	}
	

	/**			Class			**/
	private Obj current_record = null;
	
	public void visit(RecordDecl record)
	{
		TabSym.chainLocalSymbols(current_record.getType());
		TabSym.closeScope();
		
		current_record = null;
	}
	
	public void visit(RecordName rec)
	{
		String name = rec.getRecordName();
		log.info(name);
		Obj obj = TabSym.find(name);
	

		if (obj != TabSym.noObj)
		{
			report_error("Identifier '" + name + "' already exists...", rec);
		}
		
		current_record = TabSym.insert(Obj.Type, name, new Struct(Struct.Class));

		TabSym.openScope();
	}
	
	
	private Obj current_class = null;
	
	public void visit(ClassName classId)
	{
		String class_name = classId.getClassName();
		
		if(exists(class_name))
		{
			report_error("Type '" + class_name + "' already exists...", classId);
		}
		else
		{
			Obj class_obj = TabSym.insert(Obj.Type, class_name, new Struct(Struct.Class));			
			TabSym.openScope();			
			current_class = class_obj;
		}
	}
	
	public void visit(ClassDecl classDecl)
	{
		TabSym.chainLocalSymbols(current_class.getType());
		TabSym.closeScope();
		
		current_class = null;
		class_var_list.clear();
	}
	
	public void visit(Extnd extnd)
	{
		Struct extnd_struct = extnd.getType().struct;
		if (extnd_struct != null && extnd_struct.getKind() == Struct.Class && extnd_struct.getElemType() != TabSym.recordType)
		{
			Struct class_struct = current_class.getType();
			//Parent class
			Obj parent_obj = TabSym.find(extnd.getType().getTypeName());
			Struct parent_struct = parent_obj.getType();
			
			if (parent_obj == TabSym.noObj || parent_obj.getKind() != Obj.Type)
			{
				report_error("Not valid class after Extends keyword...", extnd);
			}
			
			class_struct.setElementType(parent_struct);
			
			for (Obj o : parent_struct.getMembers())
			{
				Obj obj = TabSym.insert(o.getKind(), o.getName(), o.getType()); 
				//ne treba, meni ovo edit local variable liste bice samo kada sam u DEFINICIJI metode.
				//edit_variable_list(obj);
				
				//Npr metoda ima svoje lokalne simbole, pa treba za ovu unetu metodu iz roditeljske klase, da se ulanca i to lokals iz roditeljske
				TabSym.openScope();
				for (Obj loc : o.getLocalSymbols())
				{
					TabSym.insert(loc.getKind(), loc.getName(), loc.getType());
				}
				TabSym.chainLocalSymbols(obj);
				TabSym.closeScope();
			}
			

		}
		else 
		{
			report_error("Not valid class after Extends keyword...", extnd);
		}
	}
	
	/**			Method			**/
	
	
	private int fn_param_cnt = 0;
	private Struct currMethodType = null;
	private boolean returnDetected = false;
	
	public void visit(MethodName methodName)
	{
		String m_name = methodName.getMethName();
		currentMethod = TabSym.noObj;
		
		if(!exists_currScope(m_name))
		{
			currentMethod = TabSym.insert(Obj.Meth, m_name, currentType);	
			methodName.obj = currentMethod;
			currMethodType = currentMethod.getType();
			TabSym.openScope();
			
			if (current_class != null)
			{
				class_var_list.add(currentMethod);
				Obj m_this = TabSym.insert(Obj.Var, "this", current_class.getType());
				
				//Ne treba jer je parametar funkcije
				//class_var_list.add(m_this);		
				fn_param_cnt = 1;
			}
			
			
			
			//report_info("Method - '" + m_name + "' is currently executing", methodName);
		}
		else
		{
			report_error("'" + m_name + "' already exists in symbol table...", methodName);
		}
	}
	
	public void visit(RetType retType)
	{
		currentType = retType.getType().struct;
	}
	
	public void visit(RetVoid retVoid)
	{
		currentType = TabSym.noType;
	}
	
	public void visit(MethodDecl methDecl)
	{
		log.info("Metoda " + currentMethod.getName() + " ima brj parametara: " + fn_param_cnt);
		currentMethod.setLevel(fn_param_cnt);
		
		if (currMethodType != TabSym.noType && returnDetected == false)
		{
			report_error("No return statement in method '" + currentMethod.getName() + "'...", null);
		}
		
		//da li je bila gotoLabela:
		
		TabSym.chainLocalSymbols(currentMethod);
		TabSym.closeScope();
		
		for(String s : gotolabel)
		{
			if (!label_list.contains(s))
			{
				report_error("Label '" + s + "' doesn't exist...", null);
			}
		}
		
		currentMethod = null;
		currMethodType = null;
		currentType = null;
		returnDetected = false;
		fn_param_cnt = 0;
		local_var_list.clear();
		label_list.clear();
		gotolabel.clear();
	}
	
	public void visit(SingleFormParam fnParam)
	{
		String param_name = fnParam.getParamName();
		if(!exists_currScope(param_name))
		{
			Obj o = TabSym.insert(Obj.Var, param_name, currentType);
			edit_variable_list(o);
			//edituje lokalnu variable listu
		}

		fn_param_cnt++;
	}
	
	
	/**			Expr			**/
	
	
	//SingleExpr = [-] Term
	public void visit(SingleExpr singleExpr)
	{
		singleExpr.struct = singleExpr.getTerm().struct;
		
		if (singleExpr.getOptMinus() instanceof OptMin)
		{
			if (singleExpr.struct != TabSym.intType)
			{
				report_error("Sign '-' is compatible with 'int' only...", singleExpr);
				singleExpr.struct = TabSym.noType;
			}
		}
	}
	
	public void visit(MultipleExpr multExpr)
	{
		Struct term_1 = multExpr.getExpr().struct;
		Struct term_2 = multExpr.getTerm().struct;
		
		multExpr.struct = term_1;
		
		if (term_1.getKind() != Struct.Int || term_2.getKind() != Struct.Int)
		{
			report_error("Terms need to be 'int' type...", multExpr.getParent());
			multExpr.struct = TabSym.noType;
		}
	}
	
	
	/**			Term			**/
	
	
	// SingleTerm = Factor=================
	public void visit(SingleTerm singleTerm)
	{
		singleTerm.struct = singleTerm.getFactor().struct;
	}
	
	// MultipleTerm = Term Mulop Factor
	public void visit(MultipleTerm multTerm)
	{
		Struct fact_1 = multTerm.getTerm().struct;
		Struct fact_2 = multTerm.getFactor().struct;
		
		if (fact_1.getKind() == Struct.Array)
			fact_1 = fact_1.getElemType();
		
		if (fact_2.getKind() == Struct.Array)
			fact_2 = fact_2.getElemType();
		
		
		multTerm.struct = TabSym.intType;
		
		if (fact_1.getKind() != Struct.Int || fact_2.getKind() != Struct.Int)
		{
			report_error("Factor needs to be 'int' type...", multTerm);
			multTerm.struct = TabSym.noType;
		}
	}
	
	
	/**			Factor			**/

	
	//Factor = Designator
	public void visit(FactorDes factorDesignator)
	{
		factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
		if(factorDesignator.struct.getKind() == Struct.Array)
		{
			factorDesignator.struct = factorDesignator.struct.getElemType();
		}
	}

	//Factor = Designator(Act)
	public void visit(FactorFn factorFn)
	{
		Designator des = factorFn.getDesignator();
		factorFn.struct = des.obj.getType();
		
		if (des.obj == null || des.obj.getKind() != Obj.Meth)
		{
			report_error("Not a valid method...", factorFn);
			factorFn.struct = TabSym.noType;
		}
		else
		{
			//Global method call
			if (des instanceof DelegDesign && check_visibility(des.obj) != CLASS)
			{
				report_info("Global method call - '" + ((DelegDesign)des).getIdName() + "'", factorFn);
			}
	
		}

		
		fnArgsList.clear();
		
	}
	
	//Factor = Designator(Act)
	public void visit(FactorFnAct factorFn)
	{
		Designator des = factorFn.getDesignator();
		
		if (des.obj == null || des.obj.getKind() != Obj.Meth)
		{
			report_error("Not a valid method...", factorFn);
			factorFn.struct = TabSym.noType;
		}
		else
		{
			//Global method call
			if (des instanceof DelegDesign && check_visibility(des.obj) != CLASS)
			{
				report_info("Global method call - '" + ((DelegDesign)des).getIdName() + "'", factorFn);
			}
			
			int this_cnt = 0;
			
			for (Obj o : des.obj.getLocalSymbols()) {
				if (o.getName().equals("this")) {
					this_cnt = 1;
					log.info("IMA THIS");
					break;
				}
			}
			
			
			int num_of_params = this_cnt == 0 ? des.obj.getLevel() : ((des.obj.getLevel() > 0) ? des.obj.getLevel() - 1 : 0);
			int counter = 0;
			
			
			
			//If num of args != num_of_params
			if (fnArgsList.size() != num_of_params)
			{
				log.info("FN ARGS LIST SIZe = " + fnArgsList.size() + " A PARAMETAR = " + des.obj.getLevel());
				report_error("Not compatible function arguments...", factorFn);
			}
			
			//Checking type of paramters and arguments if they are compatible
			for (Obj o : des.obj.getLocalSymbols())
			{
				if(counter == num_of_params)
					break;
				
				if(o.getName().equals("this"))
					continue;
				
				Struct curr_arg = fnArgsList.get(counter);
				if (!curr_arg.assignableTo(o.getType()))
				{
					report_error("Not compatible function arguments...", factorFn);
				}
				
				counter++;
			}
			
			factorFn.struct = des.obj.getType();
		}
		
		fnArgsList.clear();
		
	}
	
	//Factor = NUMCONST
	public void visit(FactorNum factorNumber)
	{
		factorNumber.struct = TabSym.intType;
	}
	
	//Factor = CHARCONST
	public void visit(FactorChar factorChar)
	{
		factorChar.struct = TabSym.charType;
	}
	
	//Factor = BOOLCONST
	public void visit(FactorBool factorBool)
	{
		factorBool.struct = TabSym.boolType;
	}
	
	//Factor = new Type
	public void visit(FactorNew factNew)
	{
		factNew.struct = currentType;
	}
	
	//Factor = new Type [Expr]
	public void visit(FactorNewExpr factArr)
	{
		if (factArr.getExpr().struct != TabSym.intType)
		{
			report_error("Not a number between '[]'", factArr);
		}
		
		factArr.struct = factArr.getType().struct;
	}
	
	public void visit(FactorExpr fact)
	{
		fact.struct = fact.getExpr().struct;
	}
	

	/**			DesignatorStatement 		**/
	
	
	List<Struct> fnArgsList = new ArrayList<>();
	
	//DesignatorStatement = Designator Assignop Expr
	public void visit(DesStmtEq desAssign)
	{
		Struct designator_type = desAssign.getDesignator().obj.getType();
		Struct expr_type = desAssign.getExpr().struct;
		
		if (designator_type.getKind() == Struct.Array)
		{
			if (expr_type == TabSym.nullType)
				return;
			designator_type = designator_type.getElemType();
		}
		
		if (expr_type.getKind() == Struct.Array)
		{
			expr_type = expr_type.getElemType();
		}
		
		if(!expr_type.assignableTo(designator_type))
		{
			report_error("Left and right assign types not compatible...", desAssign);
		}
	}
	
	//DesignatorStatement = (DesStmtFnAct) Designator (ActPars)
	public void visit(DesStmtFnAct desFn)
	{
		Designator des = desFn.getDesignator();
		
		if (des.obj == null || des.obj.getKind() != Obj.Meth)
		{
			report_error("Not a valid method...", desFn);
		}
		else
		{
			//Global method call
			if (des instanceof DelegDesign && check_visibility(des.obj) != CLASS)
			{
				report_info("Global method call - '" + ((DelegDesign)des).getIdName() + "'", desFn);
			}
			
			int this_cnt = 0;
			
			for (Obj o : des.obj.getLocalSymbols()) {
				if (o.getName().equals("this")) {
					this_cnt = 1;
					log.info("IMA THIS");
					break;
				}
			}
			
			
			int num_of_params = this_cnt == 0 ? des.obj.getLevel() : ((des.obj.getLevel() > 0) ? des.obj.getLevel() - 1 : 0);
			int counter = 0;
			
			//If num of args != num_of_params
			if (fnArgsList.size() != num_of_params)
			{
				report_error("Not compatible function arguments...", desFn);
			}
			else
			{
				//Checking type of paramters and arguments if they are compatible
				for (Obj o : des.obj.getLocalSymbols())
				{
					if(counter == num_of_params)
						break;
					
					if(o.getName().equals("this"))
						continue;
					
					
					Struct curr_arg = fnArgsList.get(counter);
					if (!curr_arg.assignableTo(o.getType()))
					{
						report_error("Not compatible function arguments...", desFn);
					}
					
					counter++;
				}
				
			}
			
		}
		
		
		
		fnArgsList.clear();
	}
	
	public void visit(DesStmtFn desFn)
	{
		Designator des = desFn.getDesignator();
		
		if (des.obj == null || des.obj.getKind() != Obj.Meth)
		{
			report_error("Not a valid method...", desFn);
		}
		else
		{
			//Global method call
			if (des instanceof DelegDesign && check_visibility(des.obj) != CLASS)
			{
				report_info("Global method call - '" + ((DelegDesign)des).getIdName() + "'", desFn);
			}
		}
	}
	
	public void visit(MultipleExpressions fnArgs)
	{
		fnArgsList.add(fnArgs.getExpr().struct);
	}
	
	public void visit(SingleExpression expr)
	{
		fnArgsList.add(expr.getExpr().struct);
	}
	
	public void visit(DesStmtPLL despp)
	{
		Struct des_type = despp.getDesignator().obj.getType();
		
		if (des_type != TabSym.intType)
		{
			report_error("Operator '++' can be used only on type 'int'...", despp);
		}

	}
	
	public void visit(DesStmtMINN desmm)
	{
		Struct des_type = desmm.getDesignator().obj.getType();
		
		if (des_type != TabSym.intType)
		{
			report_error("Operator '--' can be used only on type 'int'...", desmm);
		}

	}
	
	
	/**			Designator			**/
	
	
	//Designator ::= (DesignDotArray) IDENT:className DOT IDENT [Expr]
	public void visit(DesignDotArray designMember)
	{
		String object_name = designMember.getDesignIdent().getClassName();
		
		Obj temp = TabSym.find(designMember.getDesignIdent().getClassName());
		designMember.getDesignIdent().obj = temp;
		designMember.obj = TabSym.noObj;
		boolean found = false;
		
		
		/* THIS */
		if (object_name.equals("this"))
		{
			String member_name = designMember.getDesTip();

			for (Obj scope_obj : class_var_list)
			{
				log.info(scope_obj.getName());
				if (scope_obj.getName().equals(member_name))
				{
					found = true;
					designMember.obj = scope_obj;
				}
			}
			
			if (!found)
			{
				report_error("Field '" + member_name + "' is not member of class...", designMember);
				designMember.obj = TabSym.noObj;
			}
		}
		else
		{
			if(temp == TabSym.noObj)
			{
				report_error("Class variable '" + object_name + "' does not exist...", designMember.getParent());
			}
			else
			{
				//Jeste objekat neke klase
				if (temp.getType().getKind() == Struct.Class)
				{
					String member_name = designMember.getDesTip();
					Struct classStruct =  temp.getType();

					for(Obj o : classStruct.getMembers())
					{
						if (o.getName().equals(member_name) && o.getType().getKind() == Struct.Array)
						{
							found = true;
							designMember.obj = o;
						}
					}
					
					if(!found)
					{
						report_error("Field '" + member_name + "' is not member of class...", designMember);
						designMember.obj = TabSym.noObj;
					}
					else if (designMember.getExpr().struct != TabSym.intType)
					{
						report_error("Not a number between '[]'", designMember);
					}
				}	
				else
				{
					report_error("'" + object_name + "' is not an object of a class...", designMember.getParent());
				}
			}
			
		}
		
	}
	
	//Designator ::= (DesignDot) IDENT:className DOT IDENT:desTip
	public void visit(DesignDot designMember)
	{		
		String object_name = designMember.getDesignIdent().getClassName();
		
		Obj temp = TabSym.find(designMember.getDesignIdent().getClassName());
		designMember.getDesignIdent().obj = temp;
		designMember.obj = TabSym.noObj;
		boolean found = false;
		
		if (object_name.equals("this"))
		{
			String member_name = designMember.getDesTip();

			//Ako smo u klasi jos uvek, cuvam u ovoj listi trenutno deklarisane u klasi
			for (Obj scope_obj : class_var_list)
			{
				log.info(scope_obj.getName());
				if (scope_obj.getName().equals(member_name))
				{
					found = true;
					designMember.obj = scope_obj;
				}
			}
			
			if (!found)
			{
				report_error("Field '" + member_name + "' is not member of class...", designMember);
				designMember.obj = TabSym.noObj;
			}
		}
		else
		{
			if(temp == TabSym.noObj)
			{
				report_error("Class variable '" + object_name + "' does not exist...", designMember.getParent());
			}
			else
			{
				//Jeste objekat neke klase
				if (temp.getType().getKind() == Struct.Class)
				{
					String member_name = designMember.getDesTip();
					Struct classStruct =  temp.getType();
					
					for(Obj o : classStruct.getMembers())
					{
						if (o.getName().equals(member_name))
						{
							found = true;
							designMember.obj = o;
						}
					}
					
					if(!found)
					{
						report_error("Field '" + member_name + "' is not member of class...", designMember);
						designMember.obj = TabSym.noObj;
					}
				}	
				else
				{
					report_error("'" + object_name + "' is not an object of a class...", designMember.getParent());
				}
			}
			
		}
			
	
	}
	
	//Designator = IDENT '[' Expr ']'
	public void visit(DesignArrayIndex desArray)
	{
		String arr_name = desArray.getArrName();
		if(!exists(arr_name))
		{
			report_error("Identifier '" + arr_name + "' does not exist...", desArray);
		}
		
		Obj arr_obj = TabSym.find(arr_name);
		Struct arr_struct = arr_obj.getType();
		
		//Mora biti niz arr_name i mora biti Expr int
		if (arr_struct.getKind() != Struct.Array)
		{
			report_error("'" + arr_name + "' is not an array...", desArray);
		}
		
		else if ((desArray.getExpr().struct.getKind() == Struct.Array && desArray.getExpr().struct.getElemType() != TabSym.intType) || 
				(desArray.getExpr().struct.getKind() != Struct.Array && desArray.getExpr().struct != TabSym.intType))
		{
			report_error("Not a number between '[]'", desArray);
		}
		else
		{
			report_info("Array '" + arr_name + "' element access", desArray);
		}
		
		desArray.obj = arr_obj;
	}
	
	//Designator = IDENT
	public void visit(DelegDesign desId)
	{
		Obj id_obj = TabSym.find(desId.getIdName());
	
		
		if (id_obj == TabSym.noObj)
		{
			report_error("Identifier '" + desId.getIdName() + "' does not exist...", desId);
		}
		else
		{
			int visibility = check_visibility(id_obj);
			
			if (visibility == LOCAL)
			{
				report_info("Local variable '" + desId.getIdName() + "' used", desId);
			}
			else if (visibility == GLOBAL)
			{
				report_info("Global variable '" + desId.getIdName() + "' used", desId);
			}
		}
		
		if (desId.getIdName().equals("null"))
		{
			desId.obj = new Obj(Obj.Var, "null", TabSym.nullType);
		}
		else
			desId.obj = id_obj;
	}
	
	private static final int LOCAL = 0;
	private static final int GLOBAL = 1;
	private static final int CLASS = 2;
	
	private int check_visibility(Obj obj)
	{
		
		if (obj.getLevel() > 0)
			return LOCAL;
		else
			return GLOBAL;
//		for (Obj o : local_var_list)
//		{
//			if (o.equals(obj))
//			{
//				return LOCAL;
//			}
//		}
//		
//		for (Obj o : global_var_list)
//		{
//			if (o.equals(obj))
//			{
//				return GLOBAL;
//			}
//		}
//		
//		for (Obj o : class_var_list)
//		{
//			if (o.equals(obj))
//			{
//				return CLASS;
//			}
//		}
//		
		//return -1;
	}
	
	
	
	/**			Statement			**/
	
	
	private boolean do_while_state = false;
	private int do_while_level = 0;
	private List<String> gotolabel = new ArrayList<>();
	private List<String> label_list = new ArrayList<>();
	
	//SingleStatement = (StmtGoto) GOTO IDENT;	
	public void visit(StmtGoto stmt_goto)
	{
		//Called labels
		gotolabel.add(stmt_goto.getI1());
	}
	
	public void visit(Label labl)
	{
		String name = labl.getI1();
//		Obj o = TabSym.find(name);
//		
//		if (o != TabSym.noObj)
//		{
//			report_error("Identifier (label) '" + name + "' already exists...", labl);
//		}
//		
//		o = insert(Obj., )
		if(label_list.contains(name))
		{
			report_error("Identifier (label) '" + name + "' already exists...", labl);
		}
		else
		{
			label_list.add(name);			
		}
		
		
	}
	
	
	public void visit(KeywordDo doo)
	{
		do_while_state = true;
		do_while_level++;
	}
	
	public void visit(StmtDoWhile do_while)
	{
		do_while_state = false;
		do_while_level--;
	}
	
	public void visit(StmtBreak stmt_break)
	{
		if (!do_while_state)
		{
			report_error("'break;' can't be used outside DO-WHILE loop...", stmt_break);
		}
		//do_while_level--;

	}
	
	public void visit(StmtContinue stmt_cont)
	{
		if (!do_while_state)
		{
			report_error("'continue;' can't be used outside DO-WHILE loop...", stmt_cont);
		}
		//do_while_level--;
	}
	
	
	//SingleStatement = (StmtPrint) PRINT Expr;
	public void visit(StmtPrint stmt_print)
	{
		Struct expr_type = stmt_print.getExpr().struct;
		if (!checkType(expr_type, TabSym.intType) && !checkType(expr_type, TabSym.boolType)
				&& !checkType(expr_type, TabSym.charType))
		{
			report_error("print(ERROR) - not compatible type to print...", stmt_print);
		}
	}
	
	//SingleStatement = (StmtPrint) PRINT Expr;
	public void visit(StmtPrintConst stmt_print)
	{
		Struct expr_type = stmt_print.getExpr().struct;
		if (!checkType(expr_type, TabSym.intType) && !checkType(expr_type, TabSym.boolType)
				&& !checkType(expr_type, TabSym.charType))
		{
			report_error("print(ERROR, NUMCONST) - not compatible type to print...", stmt_print);
		}
	}
	
	//SingleStatement = (StmtRead) READ (Designator)
	public void visit(StmtRead stmt_read)
	{
		Designator des = stmt_read.getDesignator();
		Struct str = des.obj.getType();
		
		if (str.getKind() == Struct.Array)
		{
			str = str.getElemType();
		}
		
		if (!checkType(str, TabSym.intType) && !checkType(str, TabSym.charType)
				&& !checkType(str, TabSym.boolType))
		{
			report_error("Read(ERROR) - not compatible type to read...", stmt_read);
		}
			
	}
	
	
	//SingleStatemet = return expr;
	public void visit(StmtRetExpr retExpr)
	{
		returnDetected = true;
		Struct expr_struct = retExpr.getExpr().struct;
		if(expr_struct.getKind() == Struct.Array)
		{
			expr_struct = expr_struct.getElemType();
		}
		
		if (currentMethod.getType() == TabSym.noType)
		{
			report_error("Return Expression statement in void method...", retExpr);
		}
		else if (!currMethodType.compatibleWith(expr_struct))
		{
			report_error("Return expression types are not compatible...", retExpr);
		}
	}
	
	//SingleStatement = return;
	public void visit(StmtReturn ret)
	{
		returnDetected = true;
		//Ako je void moze samo return; da stoji
		if (currMethodType != TabSym.noType)
		{
			report_error("Return statement without expression...", ret);
		}
	}

	
	private boolean checkType(Obj obj, Struct str)
	{
		if (obj.getType().equals(str))
			return true;
		else
			return false;
	}
	
	private boolean checkType(Struct str1, Struct str)
	{	
		if (str1.equals(str))
			return true;
		else
			return false;
	}
	
	
	/**			Condition			**/
	
	
	private boolean condition_state = false;
	
	public void visit(SingleCond cond)
	{
		cond.struct = cond.getCondTerm().struct;
	}
	
	public void visit(MultipleCond cond)
	{
		cond.struct = cond.getCondTerm().struct;
	}
	
	public void visit(SingleCondTerm cond)
	{
		cond.struct = cond.getCondFact().struct;
	}
	
	public void visit(MultipleCondTerm cond)
	{
		cond.struct = cond.getCondFact().struct;
	}
	
	public void visit(CondRelOp cond)
	{
		Struct expr1 = cond.getExpr().struct;
		Struct expr2 = cond.getExpr1().struct;
		
		if (!expr1.compatibleWith(expr2))
		{
			report_error("Condition expressions are not compatible...", cond);
			cond.struct = TabSym.noType;
		}
		else
		{
			if (expr1.getKind() == Struct.Class || expr1.getKind() == Struct.Array 
					|| expr2.getKind() == Struct.Class || expr2.getKind() == Struct.Array)
			{
				if (visited_relop != EQEQ && visited_relop != DIFF)
				{
					report_error("'" + visited_relop_string + "' can't be used on these type of objects...", cond.getParent());
					cond.struct = TabSym.noType;
				}
				else
				{
					cond.struct = TabSym.boolType;
				}
			}
			
		}
		

	}
	
	public void visit(CondExpr cond)
	{
		cond.struct = TabSym.boolType;
	}
	
	
	/**			Operations			**/
	
	
	public static final int EQEQ = 0;
	public static final int DIFF = 1;
	public static final int GR = 2;
	public static final int GRE = 3;
	public static final int LS = 4;
	public static final int LSE = 5;
	private int visited_relop = -1;
	private String visited_relop_string = "";
	
	public void visit(Equalss eqeq)
	{
		visited_relop = 0;
		visited_relop_string = "==";
	}
	
	public void visit(Different diff)
	{
		visited_relop = 1;
		visited_relop_string = "!=";
	}
	
	public void visit(Greater gr)
	{
		visited_relop = 2;
		visited_relop_string = ">";
	}
	
	public void visit(GreaterEq gre)
	{
		visited_relop = 3 ;
		visited_relop_string = ">=";
	}
	
	public void visit(Lesser ls)
	{
		visited_relop = 4;
		visited_relop_string = "<";
	}
	
	public void visit(LesserEq lse)
	{
		visited_relop = 5;
		visited_relop_string = "<=";
	}
	

	
}
