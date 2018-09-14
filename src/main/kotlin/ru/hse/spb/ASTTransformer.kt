package ru.hse.spb
import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

class ASTTransformer: ExpBaseVisitor<Unit>() {
    override fun visitEval(ctx: ExpParser.EvalContext?) {
        for (child in ctx?.children!!) {
            child?.accept(this)
        }
    }

    override fun visitAdditionExp(ctx: ExpParser.AdditionExpContext?) {
        super.visitAdditionExp(ctx)
    }

    override fun visitMultiplyExp(ctx: ExpParser.MultiplyExpContext?) {
        super.visitMultiplyExp(ctx)
    }

    override fun visitAtomExp(ctx: ExpParser.AtomExpContext?) {
        super.visitAtomExp(ctx)
    }

}