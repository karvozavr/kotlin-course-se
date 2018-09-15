package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class ASTTransformerTest {

    @Test
    fun testASTTransformation() {
        val transformer = ASTTransformer()
        val expr = "1       aa"
        val parser = Parser(expr)
        val ast = parser.ast
        println(ast)
    }
}

