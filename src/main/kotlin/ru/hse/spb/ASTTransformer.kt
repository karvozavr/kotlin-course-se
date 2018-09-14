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

    override fun visitAdditionExp(ctx: ExpParser.AdditionExpContext?): Expression {
        if (ctx?.op == null) {
            return ctx?.exp?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!!
        val right = ctx.right?.accept(this)!!

        return when (ctx.op?.type) {
            ExpParser.PLUS -> BinaryExpression(left, right, BinaryExpression.Operation.PLUS)
            ExpParser.MINUS -> BinaryExpression(left, right, BinaryExpression.Operation.MINUS)
            else -> throw IllegalStateException()
        }
    }

    override fun visitMultiplyExp(ctx: ExpParser.MultiplyExpContext?): Expression {
        if (ctx?.op == null) {
            return ctx?.exp?.accept(this)!!
        }

        val left = ctx.left?.accept(this)!!
        val right = ctx.right?.accept(this)!!

        return when (ctx.op?.type) {
            ExpParser.MUL -> BinaryExpression(left, right, BinaryExpression.Operation.MUL)
            ExpParser.DIV -> BinaryExpression(left, right, BinaryExpression.Operation.DIV)
            ExpParser.MOD -> BinaryExpression(left, right, BinaryExpression.Operation.MOD)
            else -> throw IllegalStateException()
        }
    }

    override fun visitAtomExp(ctx: ExpParser.AtomExpContext?): Expression {
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
