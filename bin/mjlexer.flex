package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline + 1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline + 1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}


letter 			= [a-zA-Z]
digit 			= [0-9]
printableChar 	= [\x20-\x7E]+
ident 			= {letter} ({letter} | {digit} | _ )* 
numConst 		= {digit} ({digit})*
charConst 		= "'"{printableChar}"'"
boolConst 		= ("true" | "false")


%%



" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   	{ return new_symbol(sym.PROG, yytext()); }
"const" 		{ return new_symbol(sym.CONST, yytext()); }
"return" 		{ return new_symbol(sym.KEYWORD_RETURN, yytext()); }
"void" 			{ return new_symbol(sym.VOID, yytext()); }
"break" 		{ return new_symbol(sym.KEYWORD_BREAK, yytext()); }
"class" 		{ return new_symbol(sym.KEYWORD_CLASS, yytext()); }
"enum" 			{ return new_symbol(sym.PROG, yytext()); }
"if" 			{ return new_symbol(sym.KEYWORD_IF, yytext()); }
"else" 			{ return new_symbol(sym.KEYWORD_ELSE, yytext()); }
"do" 			{ return new_symbol(sym.KEYWORD_DO, yytext()); }
"while" 		{ return new_symbol(sym.KEYWORD_WHILE, yytext()); }
"new" 			{ return new_symbol(sym.KEYWORD_NEW, yytext()); }
"read" 			{ return new_symbol(sym.KEYWORD_READ, yytext()); }
"extends" 		{ return new_symbol(sym.KEYWORD_EXTENDS, yytext()); }
"this" 			{ return new_symbol(sym.IDENT, yytext()); }
"super" 		{ return new_symbol(sym.IDENT, yytext()); }
"goto" 			{ return new_symbol(sym.KEYWORD_GOTO, yytext()); }
"continue" 		{ return new_symbol(sym.KEYWORD_CONTINUE, yytext()); }
"record" 		{ return new_symbol(sym.RECORD, yytext()); }
"print"			{ return new_symbol(sym.KEYWORD_PRINT, yytext()); }


";" 		{ return new_symbol (sym.SEMI, yytext()); }
","			{ return new_symbol (sym.COMMA, yytext()); }
"["			{ return new_symbol(sym.LSQRB, yytext()); }
"]"			{ return new_symbol(sym.RSQRB, yytext()); }
":"			{ return new_symbol(sym.COLON, yytext()); }
"."			{ return new_symbol(sym.DOT, yytext()); }
"("			{ return new_symbol(sym.LBRACK, yytext()); }
")"			{ return new_symbol(sym.RBRACK, yytext()); }
"{"			{ return new_symbol(sym.LCURLY, yytext()); }
"}"			{ return new_symbol(sym.RCURLY, yytext()); }


"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }
"==" 		{ return new_symbol(sym.EQEQ, yytext()); }
"!=" 		{ return new_symbol(sym.DIFF, yytext()); }
">" 		{ return new_symbol(sym.GR, yytext()); }
">=" 		{ return new_symbol(sym.GRE, yytext()); }
"<" 		{ return new_symbol(sym.LS, yytext()); }
"<=" 		{ return new_symbol(sym.LSE, yytext()); }
"&&" 		{ return new_symbol(sym.ANDAND, yytext()); }
"||" 		{ return new_symbol(sym.OROR, yytext()); }
"=" 		{ return new_symbol(sym.EQ, yytext()); }
"++"		{ return new_symbol(sym.PLUSPLUS, yytext()); }
"--"		{ return new_symbol(sym.MINUSMINUS, yytext()); }


<YYINITIAL> "//" 		     { yybegin(COMMENT); }
<COMMENT> .      			 { yybegin(COMMENT); }
<COMMENT> "\r\n" 			 { yybegin(YYINITIAL); }

{numConst} 	{ return new_symbol (sym.NUMCONST, new Integer(Integer.parseInt(yytext()))); }
{charConst} { return new_symbol (sym.CHARCONST, yytext()); }
{boolConst} { return new_symbol (sym.BOOLCONST, new Boolean(Boolean.parseBoolean(yytext()))); }
{ident} 	{ return new_symbol (sym.IDENT, yytext()); }


. { System.err.println("Lexical error: (" + yytext()+ ") on line " + (yyline + 1)); }






