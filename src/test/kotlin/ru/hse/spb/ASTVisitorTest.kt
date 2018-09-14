package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class ASTVisitorTest {
    private class TestVisitor : ASTNodeVisitor {
        override fun visit(node: Block) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Function) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Variable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Expression) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: While) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: If) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Assignment) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Return) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Identifier) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: ParameterNames) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: FunctionCall) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: BinaryExpression) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Literal) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Arguments) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun visit(node: Statement) {
            println("Visited statement") //To change body of created functions use File | Settings | File Templates.
        }
    }

    @Test
    fun testVisitor() {
        val st = Statement()
        val visitor = TestVisitor()

        assertEquals("Visited statement", st.accept(visitor))
    }
}