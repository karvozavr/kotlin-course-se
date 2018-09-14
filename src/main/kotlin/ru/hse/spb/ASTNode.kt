package ru.hse.spb

interface ASTNodeVisitor {
    fun visit(node: Block)
    fun visit(node: Statement)
    fun visit(node: Function)
    fun visit(node: Variable)
    fun visit(node: Expression)
    fun visit(node: While)
    fun visit(node: If)
    fun visit(node: Assignment)
    fun visit(node: Return)
    fun visit(node: Identifier)
    fun visit(node: ParameterNames)
    fun visit(node: FunctionCall)
    fun visit(node: BinaryExpression)
    fun visit(node: Literal)
    fun visit(node: Arguments)
}

interface ASTNode {
    val children: List<ASTNode>
    fun accept(visitor: ASTNodeVisitor)
}

class Block(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Statement(statement: ASTNode) : ASTNode {
    override val children: List<ASTNode> = listOf(statement)
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Function(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Variable(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Expression(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class While(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class If(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Assignment(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Return(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Identifier(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class ParameterNames(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class FunctionCall(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class BinaryExpression(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Literal(override val children: List<ASTNode> = emptyList()) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

class Arguments(override val children: List<ASTNode>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

