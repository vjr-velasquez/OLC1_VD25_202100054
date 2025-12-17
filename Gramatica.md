print, var
int, double, string, bool
true, false
if, else
while, for, do
switch, case, default
break, continue

( ) { } ; : =
+ - * / % **
< > <= >= != ==
&& || ^ !

<program>       ::= <stmt_list>

<stmt_list>     ::= <stmt_list> <stmt>
                  | <stmt>

<stmt>          ::= <print_stmt>
                  | <decl_stmt>
                  | <assign_stmt>
                  | <if_stmt>
                  | <while_stmt>
                  | <do_while_stmt>
                  | <for_stmt>
                  | <switch_stmt>
                  | <break_stmt>
                  | <continue_stmt>

<print_stmt>    ::= "print" "(" <expr> ")" ";"

<decl_stmt>     ::= "var" IDENT ":" <type> ";" 
                  | "var" IDENT ":" <type> "=" <expr> ";"

<assign_stmt>   ::= IDENT "=" <expr> ";"

<break_stmt>    ::= "break" ";"

<continue_stmt> ::= "continue" ";"

<if_stmt>       ::= "if" "(" <expr> ")" "{" <stmt_list> "}"
                  | "if" "(" <expr> ")" "{" <stmt_list> "}"
                    "else" "{" <stmt_list> "}"

<switch_stmt>   ::= "switch" "(" <expr> ")" "{" <case_list> <default_opt> "}"

<case_list>     ::= <case_list> <case_item>
                  | <case_item>
                  | /* vacío */

<case_item>     ::= "case" <expr> ":" "{" <stmt_list> "}"

<default_opt>   ::= "default" ":" "{" <stmt_list> "}"
                  | /* vacío */

<while_stmt>    ::= "while" "(" <expr> ")" "{" <stmt_list> "}"

<do_while_stmt> ::= "do" "{" <stmt_list> "}"
                    "while" "(" <expr> ")" ";"


<for_stmt>      ::= "for" "(" <for_init> ";" <expr> ";" <for_update> ")"
                    "{" <stmt_list> "}"

<for_init>      ::= IDENT "=" <expr>
<for_update>    ::= IDENT "=" <expr>

<type>          ::= "int"
                  | "double"
                  | "string"
                  | "bool"

<expr>          ::= <or_expr>

/* OR */
<or_expr>       ::= <or_expr> "||" <xor_expr>
                  | <xor_expr>

/* XOR */
<xor_expr>      ::= <xor_expr> "^" <and_expr>
                  | <and_expr>

/* AND */
<and_expr>      ::= <and_expr> "&&" <rel_expr>
                  | <rel_expr>

/* Relacionales: ==, !=, <, >, <=, >= */
<rel_expr>      ::= <rel_expr> "==" <add_expr>
                  | <rel_expr> "!=" <add_expr>
                  | <rel_expr> "<"  <add_expr>
                  | <rel_expr> ">"  <add_expr>
                  | <rel_expr> "<=" <add_expr>
                  | <rel_expr> ">=" <add_expr>
                  | <add_expr>

/* Suma y resta */
<add_expr>      ::= <add_expr> "+" <mul_expr>
                  | <add_expr> "-" <mul_expr>
                  | <mul_expr>

/* *, /, % y ** */
<mul_expr>      ::= <mul_expr> "*"  <unary_expr>
                  | <mul_expr> "/"  <unary_expr>
                  | <mul_expr> "%"  <unary_expr>
                  | <mul_expr> "**" <unary_expr>
                  | <unary_expr>

/* Unarios: signo y NOT */
<unary_expr>    ::= "-" <unary_expr>
                  | "!" <unary_expr>
                  | <primary>

/* Primarios: paréntesis, literales, identificadores */
<primary>       ::= "(" <expr> ")"
                  | INT_LIT
                  | DOUBLE_LIT
                  | CHAR_LIT
                  | STRING_LIT
                  | "true"
                  | "false"
                  | IDENT



