package ru.hse.spb

import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpParser

class ASTTransformer : ExpBaseVisitor<ASTNode>() {
    override fun visitFile(ctx: ExpParser.FileContext?): ASTNode {
        return ctx?.mainBlock?.accept(this)!!
    }

    override fun visitBlock(ctx: ExpParser.BlockContext?): ASTNode {
        return Block(ctx?.children?.map { x -> x.accept(this) as Statement }!!)
    }

    override fun visitStatement(ctx: ExpParser.StatementContext?): ASTNode {
        return ctx?.children!![0].accept(this)
    }

    override fun visitFunctionDefinition(ctx: ExpParser.FunctionDefinitionContext?): ASTNode {
        val name: String = ctx?.name?.text!!
        val params = ctx.params.accept(this) as ParameterNames
        val body = ctx.body.accept(this) as Block

        return Function(Identifier(name), params, body)
    }

    override fun visitVariableDeclaration(ctx: ExpParser.VariableDeclarationContext?): ASTNode {
        return super.visitVariableDeclaration(ctx)
    }

    override fun visitParameterNames(ctx: ExpParser.ParameterNamesContext?): ASTNode {
        return super.visitParameterNames(ctx)
    }

    override fun visitWhileLoop(ctx: ExpParser.WhileLoopContext?): ASTNode {
        return super.visitWhileLoop(ctx)
    }

    override fun visitConditional(ctx: ExpParser.ConditionalContext?): ASTNode {
        return super.visitConditional(ctx)
    }

    override fun visitAssignment(ctx: ExpParser.AssignmentContext?): ASTNode {
        val identifier: String = ctx?.identifier?.text!!
        val value: ASTNode = ctx.value.accept(this)
        return Assignment(Identifier(identifier), value)
    }

    override fun visitReturnStatement(ctx: ExpParser.ReturnStatementContext?): ASTNode {
        return super.visitReturnStatement(ctx)
    }

    override fun visitExpression(ctx: ExpParser.ExpressionContext?): ASTNode {
        return super.visitExpression(ctx)
    }

    override fun visitFunctionCall(ctx: ExpParser.FunctionCallContext?): ASTNode {
        return super.visitFunctionCall(ctx)
    }

    override fun visitArguments(ctx: ExpParser.ArgumentsContext?): ASTNode {
        return super.visitArguments(ctx)
    }

    override fun visitGeneralExp(ctx: ExpParser.GeneralExpContext?): ASTNode {
        if (ctx?.op == null) {
            return ctx?.left?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!! as Expression
        val right = ctx.right?.accept(this)!! as Expression

        return when (ctx.op?.type) {
            ExpParser.LE -> BinaryExpression(left, right, BinaryExpression.Operation.LE)
            ExpParser.GR -> BinaryExpression(left, right, BinaryExpression.Operation.GR)
            ExpParser.GEQ -> BinaryExpression(left, right, BinaryExpression.Operation.GEQ)
            ExpParser.LEQ -> BinaryExpression(left, right, BinaryExpression.Operation.LEQ)
            ExpParser.EQ -> BinaryExpression(left, right, BinaryExpression.Operation.EQ)
            ExpParser.NEQ -> BinaryExpression(left, right, BinaryExpression.Operation.NEQ)
            ExpParser.AND -> BinaryExpression(left, right, BinaryExpression.Operation.AND)
            ExpParser.OR -> BinaryExpression(left, right, BinaryExpression.Operation.OR)
            else -> throw IllegalStateException()
        }
    }

    override fun visitAdditionExp(ctx: ExpParser.AdditionExpContext?): ASTNode {
        if (ctx?.op == null) {
            return ctx?.exp?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!! as Expression
        val right = ctx.right?.accept(this)!! as Expression

        return when (ctx.op?.type) {
            ExpParser.PLUS -> BinaryExpression(left, right, BinaryExpression.Operation.PLUS)
            ExpParser.MINUS -> BinaryExpression(left, right, BinaryExpression.Operation.MINUS)
            else -> throw IllegalStateException()
        }
    }

    override fun visitMultiplyExp(ctx: ExpParser.MultiplyExpContext?): ASTNode {
        if (ctx?.op == null) {
            return ctx?.exp?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!! as Expression
        val right = ctx.right?.accept(this)!! as Expression

        return when (ctx.op?.type) {
            ExpParser.MUL -> BinaryExpression(left, right, BinaryExpression.Operation.MUL)
            ExpParser.DIV -> BinaryExpression(left, right, BinaryExpression.Operation.DIV)
            ExpParser.MOD -> BinaryExpression(left, right, BinaryExpression.Operation.MOD)
            else -> throw IllegalStateException()
        }
    }

    override fun visitAtomExp(ctx: ExpParser.AtomExpContext?): ASTNode {
        val identifier: String? = ctx?.identifier?.text
        val literal: String? = ctx?.literal?.text
        val exp = ctx?.exp

        if (literal != null) {
            return Literal(literal.toInt())
        }

        if (identifier != null) {
            return Identifier(identifier)
        }

        if (exp != null) {
            return exp.accept(this)
        }

        throw IllegalStateException()
    }
}
