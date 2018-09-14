grammar Exp;


eval
    :    exp=generalExp
    ;

generalExp
    :     left=additionExp
        | left=additionExp op=LE right=generalExp
        | left=additionExp op=GR right=generalExp
        | left=additionExp op=GEQ right=generalExp
        | left=additionExp op=LEQ right=generalExp
        | left=additionExp op=EQ right=generalExp
        | left=additionExp op=NEQ right=generalExp
        | left=additionExp op=AND right=generalExp
        | left=additionExp op=OR right=generalExp
    ;

additionExp
    :      left=multiplyExp
         | left=multiplyExp op=PLUS right=additionExp
         | left=multiplyExp op=MINUS right=additionExp
    ;

multiplyExp
    :      left=atomExp
         | left=atomExp op=MUL right=multiplyExp
         | left=atomExp op=DIV right=multiplyExp
         | left=atomExp op=MOD right=multiplyExp
    ;

atomExp
    :    value=Number
    |    '(' exp=generalExp ')'
    ;


Number
    :    ('0'..'9')+ ('.' ('0'..'9')+)?
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
