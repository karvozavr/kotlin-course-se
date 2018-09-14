package ru.hse.spb

interface ASTNodeVisitor<out T> {
    fun visit(node: Block): T
    fun visit(node: Function): T
    fun visit(node: Variable): T
    fun visit(node: Expression): T
    fun visit(node: While): T
    fun visit(node: If): T
    fun visit(node: Assignment): T
    fun visit(node: Return): T
    fun visit(node: Identifier): T
    fun visit(node: ParameterNames): T
    fun visit(node: FunctionCall): T
    fun visit(node: BinaryExpression): T
    fun visit(node: Literal): T
    fun visit(node: Arguments): T
    fun visit(node: ASTNode): T
}

interface ASTNode {
    fun accept(visitor: ASTNodeVisitor<*>)
}

class Block(val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

interface Statement : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Function(val identifier: Identifier, val parameterNames: ParameterNames, val body: Block) : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Variable : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

interface Expression : Statement

class While : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class If : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Assignment : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Return : Statement {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Identifier : Expression {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class ParameterNames : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class FunctionCall : Expression {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class BinaryExpression(leftArgument: Expression, rightArgument: Expression, val operation: Operation) : Expression {
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

    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Literal(val value: Int) : Expression {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

class Arguments(val arguments: List<Expression>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor<*>) {
        visitor.visit(this)
    }
}

