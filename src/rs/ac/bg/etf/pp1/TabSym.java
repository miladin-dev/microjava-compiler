package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class TabSym extends Tab {
	
	public static final Struct boolType = new Struct(Struct.Bool);
	public static final Struct classType = new Struct(Struct.Class);
	public static final Struct recordType = new Struct(Struct.None);
	
	// dump
	

}
