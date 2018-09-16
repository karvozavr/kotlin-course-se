package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class ASTTest {
    @Test
    fun testEquals() {
        val ast1 = BinaryExpression(Identifier("aa"), Literal(2), BinaryExpression.Operation.AND)
        val ast2 = BinaryExpression(Identifier("aa"), Literal(2), BinaryExpression.Operation.AND)
        val ast3 = BinaryExpression(Identifier("bb"), Literal(2), BinaryExpression.Operation.AND)
        assertEquals(ast1, ast2)
        assertNotEquals(ast1, ast3)
    }
}