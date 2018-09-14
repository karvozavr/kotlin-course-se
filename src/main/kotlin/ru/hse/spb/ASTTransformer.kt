package ru.hse.spb

import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpParser

class ASTTransformer : ExpBaseVisitor<Expression>() {
    override fun visitEval(ctx: ExpParser.EvalContext?): Expression {
        return ctx?.exp?.accept(this)!!
    }

    override fun visitGeneralExp(ctx: ExpParser.GeneralExpContext?): Expression {
        if (ctx?.op == null) {
            return ctx?.left?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!!
        val right = ctx.right?.accept(this)!!

        return when (ctx.op?.tokenIndex) {
            ExpParser.LE -> BinaryExpression(left, right, BinaryExpression.Operation.LE)
            ExpParser.GR -> BinaryExpression(left, right, BinaryExpression.Operation.GR)
            ExpParser.GEQ -> BinaryExpression(left, right, BinaryExpression.Operation.GEQ)
            ExpParser.LEQ -> BinaryExpression(left, right, BinaryExpression.Operation.LEQ)
            ExpParser.EQ -> BinaryExpression(left, right, BinaryExpression.Operation.EQ)
            ExpParser.NEQ -> BinaryExpression(left, right, BinaryExpression.Operation.NEQ)
            ExpParser.AND -> BinaryExpression(left, right, BinaryExpression.Operation.AND)
            ExpParser.OR -> BinaryExpression(left, right, BinaryExpression.Operation.OR)
            else -> throw NotImplementedError()
        }
    }

    override fun visitAdditionExp(ctx: ExpParser.AdditionExpContext?): Expression {
        if (ctx?.op == null) {
            return ctx?.left?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!!
        val right = ctx.right?.accept(this)!!

        return when (ctx.op?.tokenIndex) {
            ExpParser.PLUS -> BinaryExpression(left, right, BinaryExpression.Operation.PLUS)
            ExpParser.GR -> BinaryExpression(left, right, BinaryExpression.Operation.MINUS)
            else -> throw NotImplementedError()
        }
    }

    override fun visitMultiplyExp(ctx: ExpParser.MultiplyExpContext?): Expression {
        return Identifier()
    }

    override fun visitAtomExp(ctx: ExpParser.AtomExpContext?): Expression {
        return Identifier()
    }

    fun buildGeneralExpression(nodes: List<ExpParser.GeneralExpContext>): Expression {
        val left = nodes[0]
        while (nodes.size > 1) { // at least 3
            val (_, op, right) = nodes
            nodes.drop(3)
        }
        // size is exactly 1
        val arg = nodes[0]
        throw NotImplementedError()
    }

}
