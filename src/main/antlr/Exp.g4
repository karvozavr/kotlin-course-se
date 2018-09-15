grammar Exp;


file
    :   mainBlock=block EOF
    ;

block
    :   (statements=statement)*
    ;

statement
    :     functionDefinition
        | variableDeclaration
        | expression
        | whileLoop
        | conditional
        | assignment
        | returnStatement
    ;

functionDefinition
    :    'fun' name=Identifier '(' params=parameterNames ')' '{' body=block '}'
    ;

variableDeclaration
    :    'var' name=Identifier ('=' value=expression)?
    ;

parameterNames
    :    params=Identifier (',' params=Identifier)*
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

expression
    :     generalExp
        | functionCall
        | Identifier
        | Literal
        | '(' expression ')'
    ;

functionCall
    :     Identifier '(' arguments ')'
    ;

arguments
    :     args=expression (',' args=expression)*
    ;

generalExp
    :     left=additionExp
        | left=additionExp (op=LE | op=GR | op=GEQ | op=LEQ | op=EQ | op=NEQ | op=AND | op=OR) right=generalExp
    ;

additionExp
    :     exp=multiplyExp
        | <assoc=left> left=additionExp (op=PLUS | op=MINUS) right=additionExp
    ;

multiplyExp
    :     exp=atomExp
        | <assoc=left> left=multiplyExp (op=MUL | op=DIV | op=MOD) right=multiplyExp
    ;

atomExp
    :     literal=Literal
        | identifier=Identifier
        | call=functionCall
        | '(' exp=generalExp ')'
    ;

Identifier
    :
        ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
    ;

Literal
    :   ('1'..'9') ('0'..'9')*
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

