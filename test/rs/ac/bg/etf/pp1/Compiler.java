package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;
import rs.ac.bg.etf.pp1.util.RuleVisitor;
import rs.ac.bg.etf.pp1.ast.Program;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void tsdump(SymbolTableVisitor stv) {
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
		if (stv == null)
			stv = new TabSymDump();
		for (Scope s = TabSym.currentScope; s != null; s = s.getOuter()) {
			s.accept(stv);
		}
		System.out.println(stv.getOutput());
	}
	
	/** Stampa sadrzaj tabele simbola. */
	public static void tsdump() {
		tsdump(null);
	}
	
	
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(Compiler.class);
//		if (args.length < 2) {
//			log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
//			return;
//		}
		
//		File sourceCode = new File(args[0]);
//		if (!sourceCode.exists()) {
//			log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
//			return;
//		}
//			
//		log.info("Compiling source file: " + sourceCode.getAbsolutePath());
		
		try (BufferedReader br = new BufferedReader(new FileReader("test/semantic_test1.mj"))) {
			Yylex lexer = new Yylex(br);
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        SyntaxNode prog = (SyntaxNode)(s.value);
	        Program pp = (Program)(s.value);
	       
	        log.info(pp.toString(""));
	        
	        if(p.unrecoveredError)
	        {
	        	log.info("Syntax error - Compiler");
	        	//return;
	        }
	        
	        TabSym.init();
	        
	        SemanticAnalyzer semanticCheck = new SemanticAnalyzer();
	        prog.traverseBottomUp(semanticCheck);
	        
	        tsdump();
	        
	        if (!semanticCheck.errorDetected && !p.errorDetected)
	        {
	        	CodeGenerator codeGen = new CodeGenerator();
	        	prog.traverseBottomUp(codeGen);

	        	Code.dataSize = semanticCheck.getnVars();
	        	Code.mainPc = codeGen.getMainPc();
	        	
	        	File objFile = new File("test/program1.obj");
	        	
	        	Code.write(new FileOutputStream(objFile));
	        }
	        else
	        {
	        	log.info("Postoji greska u prevodjenju.");
	        }
	        
	        
//			Tab.init(); // Universe scope
//			SemanticPass semanticCheck = new SemanticPass();
//			prog.traverseBottomUp(semanticCheck);
//			
//	        log.info("Print calls = " + semanticCheck.printCallCount);
//	        Tab.dump();
//	        
//	        if (!p.errorDetected && semanticCheck.passed()) {
//	        	File objFile = new File(args[1]);
//	        	log.info("Generating bytecode file: " + objFile.getAbsolutePath());
//	        	if (objFile.exists())
//	        		objFile.delete();
//	        	
//	        	// Code generation...
//	        	CodeGenerator codeGenerator = new CodeGenerator();
//	        	prog.traverseBottomUp(codeGenerator);
//	        	Code.dataSize = semanticCheck.nVars;
//	        	Code.mainPc = codeGenerator.getMainPc();
//	        	Code.write(new FileOutputStream(objFile));
//	        	log.info("Parsiranje uspesno zavrseno!");
//	        }
//	        else {
//	        	log.error("Parsiranje NIJE uspesno zavrseno!");
//	        }
		}
	}
}
