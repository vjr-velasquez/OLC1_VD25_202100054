package analisis;

import java_cup.runtime.Symbol;
import excepciones.ControlErrores;
import excepciones.Errores;
import java.util.LinkedList;
import java_cup.runtime.Symbol;

%%

%{

    // lista global de tokens del scanner
    public static LinkedList<Token> listaTokens = new LinkedList<>();

    private void agregarToken(String tipo) {
        listaTokens.add(new Token(tipo, yytext(), yyline, yycolumn));
    }

%}


%init{
    yyline = 1;
    yycolumn = 1;
%init}

%cup
%class scanner
%public 
%line
%char
%column
%full
%ignorecase


// ------------ Expresiones regulares ------------

// símbolos simples
PAR1       = "("
PAR2       = ")"
MAS        = "+"
MENOS      = "-"
IGUAL      = "="
POR        = "*"
DIV        = "/"
MOD        = "%"
POT        = "**"

// llaves para bloques
LLAVEIZQ   = "{"
LLAVEDER   = "}"

// lógicos
AND        = "&&"
OR         = "||"
XOR        = "^"
NOT        = "!"

// relacionales
MENORIGUAL = "<="
MAYORIGUAL = ">="
DIFERENTE  = "!="
MENORQ     = "<"
MAYORQ     = ">"

FINCADENA  = ";"

// espacios y comentarios
BLANCOS      = [\ \r\t\f\n]+
COMENT_LINEA = "//".*
DOSPUNTOS    = ":"

// literales
ENTERO   = [0-9]+
DECIMAL  = [0-9]+"."[0-9]+
CHAR     = \'([^\'\\]|\\.)\'        
CADENA   = \"([^\"\\]|\\.)*\"          

ID       = [a-zA-Z_][a-zA-Z0-9_]*

// palabras reservadas
PRINT  = "print"
TRUE   = "true"
FALSE  = "false"
INT    = "int"
DOUBLE = "double"
STRING = "string"
VAR    = "var"
BOOL   = "bool"

IF     = "if"
ELSE   = "else"
WHILE  = "while"
FOR    = "for"
BREAK  = "break"
CONTINUE = "continue"

SWITCH  = "switch"
CASE    = "case"
DEFAULT = "default"


%%
// ------------ Reglas léxicas ------------

// espacios y comentarios se ignoran
<YYINITIAL> {BLANCOS}      { /* ignorar espacios */ }
<YYINITIAL> {COMENT_LINEA} { /* ignorar comentario de línea */ }

// palabras reservadas
<YYINITIAL> {PRINT}   { agregarToken("PRINT");   return new Symbol(sym.PRINT,   yyline, yycolumn, yytext()); }
<YYINITIAL> {INT}     { agregarToken("INT");     return new Symbol(sym.INT,     yyline, yycolumn, yytext()); }
<YYINITIAL> {DOUBLE}  { agregarToken("DOUBLE");  return new Symbol(sym.DOUBLE,  yyline, yycolumn, yytext()); }
<YYINITIAL> {STRING}  { agregarToken("STRING");  return new Symbol(sym.STRING,  yyline, yycolumn, yytext()); }
<YYINITIAL> {VAR}     { agregarToken("VAR");     return new Symbol(sym.VAR,     yyline, yycolumn, yytext()); }
<YYINITIAL> {BOOL}    { agregarToken("BOOL");    return new Symbol(sym.BOOL,    yyline, yycolumn, yytext()); }
<YYINITIAL> {TRUE}    { agregarToken("TRUE");    return new Symbol(sym.TRUE,    yyline, yycolumn, yytext()); }
<YYINITIAL> {FALSE}   { agregarToken("FALSE");   return new Symbol(sym.FALSE,   yyline, yycolumn, yytext()); }
<YYINITIAL> {IF}      { agregarToken("IF");      return new Symbol(sym.IF,      yyline, yycolumn, yytext()); }
<YYINITIAL> {ELSE}    { agregarToken("ELSE");    return new Symbol(sym.ELSE,    yyline, yycolumn, yytext()); }
<YYINITIAL> {WHILE}   { agregarToken("WHILE");   return new Symbol(sym._while,  yyline, yycolumn, yytext()); }
<YYINITIAL> {FOR}     { agregarToken("FOR");     return new Symbol(sym._for,    yyline, yycolumn, yytext()); }
<YYINITIAL> {BREAK}   { agregarToken("BREAK");   return new Symbol(sym._break,  yyline, yycolumn, yytext()); }
<YYINITIAL> {CONTINUE} { agregarToken("CONTINUE"); return new Symbol(sym._continue, yyline, yycolumn, yytext()); }
<YYINITIAL> {SWITCH}  { agregarToken("SWITCH");  return new Symbol(sym.SWITCH,  yyline, yycolumn, yytext()); }
<YYINITIAL> {CASE}    { agregarToken("CASE");    return new Symbol(sym.CASE,    yyline, yycolumn, yytext()); }
<YYINITIAL> {DEFAULT} { agregarToken("DEFAULT"); return new Symbol(sym.DEFAULT, yyline, yycolumn, yytext()); }




// identificadores
<YYINITIAL> {ID}      { agregarToken("ID");      return new Symbol(sym.ID,      yyline, yycolumn, yytext()); }

// números y cadenas
<YYINITIAL> {DECIMAL} { agregarToken("DECIMAL"); return new Symbol(sym.DECIMAL, yyline, yycolumn, yytext()); }
<YYINITIAL> {ENTERO}  { agregarToken("ENTERO");  return new Symbol(sym.ENTERO,  yyline, yycolumn, yytext()); }

<YYINITIAL> {CHAR} {
    String texto = yytext();
    char contenido = texto.charAt(1);
    agregarToken("CHAR");
    return new Symbol(sym.CHAR, yyline, yycolumn, contenido);
}

<YYINITIAL> {CADENA} {
    String cadena = yytext();
    cadena = cadena.substring(1, cadena.length() - 1);
    agregarToken("CADENA");
    return new Symbol(sym.CADENA, yyline, yycolumn, cadena);
}

// símbolos de agrupación y fin de sentencia
<YYINITIAL> {PAR1}      { agregarToken("PAR1");      return new Symbol(sym.PAR1,      yyline, yycolumn, yytext()); }
<YYINITIAL> {PAR2}      { agregarToken("PAR2");      return new Symbol(sym.PAR2,      yyline, yycolumn, yytext()); }
<YYINITIAL> {FINCADENA} { agregarToken("FINCADENA"); return new Symbol(sym.FINCADENA, yyline, yycolumn, yytext()); }
<YYINITIAL> {DOSPUNTOS} { agregarToken("DOSPUNTOS"); return new Symbol(sym.DOSPUNTOS, yyline, yycolumn, yytext()); }
<YYINITIAL> {IGUAL}     { agregarToken("IGUAL");     return new Symbol(sym.IGUAL,     yyline, yycolumn, yytext()); }
<YYINITIAL> {LLAVEIZQ}  { agregarToken("LLAVEIZQ");  return new Symbol(sym.LLAVEIZQ,  yyline, yycolumn, yytext()); }
<YYINITIAL> {LLAVEDER}  { agregarToken("LLAVEDER");  return new Symbol(sym.LLAVEDER,  yyline, yycolumn, yytext()); }

// aritméticos
<YYINITIAL> {MAS}   { agregarToken("MAS");   return new Symbol(sym.MAS,   yyline, yycolumn, yytext()); }
<YYINITIAL> {MENOS} { agregarToken("MENOS"); return new Symbol(sym.MENOS, yyline, yycolumn, yytext()); }
<YYINITIAL> {POR}   { agregarToken("POR");   return new Symbol(sym.POR,   yyline, yycolumn, yytext()); }
<YYINITIAL> {DIV}   { agregarToken("DIV");   return new Symbol(sym.DIV,   yyline, yycolumn, yytext()); }
<YYINITIAL> {MOD}   { agregarToken("MOD");   return new Symbol(sym.MOD,   yyline, yycolumn, yytext()); }
<YYINITIAL> {POT}   { agregarToken("POT");   return new Symbol(sym.POT,   yyline, yycolumn, yytext()); }

// relacionales
<YYINITIAL> {MENORIGUAL} { agregarToken("MENORIGUAL"); return new Symbol(sym.MENORIGUAL, yyline, yycolumn, yytext()); }
<YYINITIAL> {MAYORIGUAL} { agregarToken("MAYORIGUAL"); return new Symbol(sym.MAYORIGUAL, yyline, yycolumn, yytext()); }
<YYINITIAL> {DIFERENTE}  { agregarToken("DIFERENTE");  return new Symbol(sym.DIFERENTE,  yyline, yycolumn, yytext()); }
<YYINITIAL> {MENORQ}     { agregarToken("MENORQ");     return new Symbol(sym.MENORQ,     yyline, yycolumn, yytext()); }
<YYINITIAL> {MAYORQ}     { agregarToken("MAYORQ");     return new Symbol(sym.MAYORQ,     yyline, yycolumn, yytext()); }

// lógicos
<YYINITIAL> {AND} { agregarToken("AND"); return new Symbol(sym.AND, yyline, yycolumn, yytext()); }
<YYINITIAL> {OR}  { agregarToken("OR");  return new Symbol(sym.OR,  yyline, yycolumn, yytext()); }
<YYINITIAL> {XOR} { agregarToken("XOR"); return new Symbol(sym.XOR, yyline, yycolumn, yytext()); }
<YYINITIAL> {NOT} { agregarToken("NOT"); return new Symbol(sym.NOT, yyline, yycolumn, yytext()); }

// cualquier otro carácter es error léxico
. {
    String desc = "Símbolo no reconocido: " + yytext();
    System.out.println("Error Léxico: " + desc +
                       " | Fila:" + yyline +
                       " | Columna:" + yycolumn);

    Errores err = new Errores("LEXICO", desc, yyline, yycolumn);
    ControlErrores.agregarError(err);
}

