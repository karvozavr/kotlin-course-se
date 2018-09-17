package ru.hse.spb

import java.util.concurrent.locks.Condition

interface ASTNodeVisitor<out StatementResult, out ExpressionResult> {
    fun visit(node: Block): StatementResult
    fun visit(node: Function): StatementResult
    fun visit(node: VariableDeclaration): StatementResult
    fun visit(node: While): StatementResult
    fun visit(node: If): StatementResult
    fun visit(node: Assignment): StatementResult
    fun visit(node: Return): StatementResult
    fun visit(node: Expression): ExpressionResult
    fun visit(node: Identifier): ExpressionResult
    fun visit(node: FunctionCall): ExpressionResult
    fun visit(node: BinaryExpression): ExpressionResult
    fun visit(node: Literal): ExpressionResult
    fun visit(node: ASTNode) {}
}

interface ASTNode {
    fun accept(visitor: ASTNodeVisitor<*, *>)
}

data class Block(val statements: List<Statement>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

interface Statement : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Function(val name: Identifier, val parameterNames: ParameterNames, val body: Block) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class VariableDeclaration(val name: Identifier, val value: Expression) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

interface Expression : Statement

data class While(val condition: Expression, val body: Block) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class If(val condition: Expression, val ifTrue: Block, val ifFalse: Block = Block(emptyList())) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Assignment(val identifier: Identifier, val value: Expression) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Return(val value: Expression) : Statement {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Identifier(val name: String) : Expression {
    var value: Int = 0

    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class ParameterNames(val params: List<Identifier> = emptyList()) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class FunctionCall(val name: Identifier, val args: Arguments) : Expression {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class BinaryExpression(val leftArgument: Expression, val rightArgument: Expression, val operation: Operation) : Expression {
    enum class Operation(func: (Int, Int) -> Int) {
        PLUS({ x, y -> x + y }),
        MINUS({ x, y -> x - y }),
        MUL({ x, y -> x * y }),
        DIV({ x, y -> x / y }),
        MOD({ x, y -> x / y }),
        LE({ x, y -> if (x < y) 1 else 0 }),
        GR({ x, y -> if (x > y) 1 else 0 }),
        LEQ({ x, y -> if (x <= y) 1 else 0 }),
        GEQ({ x, y -> if (x >= y) 1 else 0 }),
        EQ({ x, y -> if (x == y) 1 else 0 }),
        NEQ({ x, y -> if (x != y) 1 else 0 }),
        AND({ x, y -> if (x != 0 && y != 0) 1 else 0 }),
        OR({ x, y -> if (x != 0 || y != 0) 1 else 0 })
    }

    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Literal(val value: Int) : Expression {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

data class Arguments(val arguments: List<Expression>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<* ,*>) {
        visitor.visit(this)
    }
}

