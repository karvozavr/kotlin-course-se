package ru.hse.spb

sealed class ASTNode

data class Block(val statements: List<Statement>) : ASTNode()

sealed class Statement : ASTNode()

data class Function(val identifier: Identifier, val parameterNames: ParameterNames, val body: Block) : Statement()

data class VariableDeclaration(val identifier: Identifier, val value: Expression) : Statement()

sealed class Expression : Statement()

data class While(val condition: Expression, val body: Block) : Statement()

data class If(val condition: Expression, val ifTrue: Block, val ifFalse: Block = Block(emptyList())) : Statement()

data class Assignment(val identifier: Identifier, val value: Expression) : Statement()

data class Return(val value: Expression) : Statement()

data class Identifier(val name: String) : Expression()

data class ParameterNames(val params: List<Identifier> = emptyList()) : ASTNode()

data class FunctionCall(val identifier: Identifier, val args: Arguments) : Expression()

data class BinaryExpression(val leftArgument: Expression, val rightArgument: Expression, val operation: Operation) : Expression() {
    enum class Operation(private val operation: (Int, Int) -> Int) {
        PLUS({ x, y -> x + y }),
        MINUS({ x, y -> x - y }),
        MUL({ x, y -> x * y }),
        DIV({ x, y -> x / y }),
        REM({ x, y -> x % y }),
        LE({ x, y -> if (x < y) 1 else 0 }),
        GR({ x, y -> if (x > y) 1 else 0 }),
        LEQ({ x, y -> if (x <= y) 1 else 0 }),
        GEQ({ x, y -> if (x >= y) 1 else 0 }),
        EQ({ x, y -> if (x == y) 1 else 0 }),
        NEQ({ x, y -> if (x != y) 1 else 0 }),
        AND({ x, y -> if (x != 0 && y != 0) 1 else 0 }),
        OR({ x, y -> if (x != 0 || y != 0) 1 else 0 });

        operator fun invoke(x: Int, y: Int) = operation(x, y)
    }
}

data class Literal(val value: Int) : Expression()

data class Arguments(val arguments: List<Expression>) : ASTNode()

