grammar Exp;


eval
    :    exp=generalExp
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


// TODO
functionCall
    :
        'function'
    ;

Identifier
    :
        ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
    ;

Literal
    :   ('1'..'9') ('0'..'9')*
    ;

WHITESPACE : (' ' | '\t' | '\r'| '\n' | '//' .* '\n') -> skip;

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
