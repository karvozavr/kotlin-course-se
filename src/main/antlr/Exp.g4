grammar Exp;


file
    :   mainBlock=block EOF
    ;

block
    :   (statement)*
    ;

statement
    :     functionDefinition
        | variableDeclaration
        | whileLoop
        | conditional
        | assignment
        | returnStatement
        | expression
    ;

functionDefinition
    :    'fun' name=Identifier '(' params=parameterNames ')' '{' body=block '}'
    ;

variableDeclaration
    :    'var' name=Identifier ('=' value=expression)?
    ;

parameterNames
    :    (Identifier (',' Identifier)*)?
    ;

whileLoop
    :    'while' '(' cond=expression ')' '{' body=block '}'
    ;

conditional
    :    'if' '(' cond=expression ')'  '{' ifTrue=block '}' ('else' '{' ifFalse=block '}')?
    ;

assignment
    :    identifier=Identifier '=' value=expression
    ;

returnStatement
    :    'return' value=expression
    ;

functionCall
    :     name=Identifier '(' args=arguments ')'
    ;

arguments
    :     args=expression (',' args=expression)*
    ;

expression
    :     call=functionCall
        | identifier=Identifier
        | literal=Literal
        | '(' exp=expression ')'
        | <assoc=left> left=expression (op=MUL | op=DIV | op=MOD) right=expression
        | <assoc=left> left=expression (op=PLUS | op=MINUS) right=expression
        | left=expression (op=LE | op=GR | op=GEQ | op=LEQ) right=expression
        | left=expression ( op=EQ | op=NEQ) right=expression
        | left=expression op=AND right=expression
        | left=expression op=OR right=expression
    ;

Identifier
    :
        ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
    ;

Literal
    :   '-'? ('1'..'9') ('0'..'9')* | '0'
    ;

WHITESPACE : (' ' | '\t' | '\r'| '\n' | '//' (.)*? '\n') -> skip;

PLUS: '+';
MUL: '*';
MINUS: '-';
DIV: '/';
MOD: '%';
AND: '&&';
OR: '||';
LE: '<';
GR: '>';
LEQ: '<=';
GEQ: '>=';
EQ: '==';
NEQ: '!=';

