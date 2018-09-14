package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class ASTTransformerTest {

    @Test
    fun testASTTransformation() {
        val transformer = ASTTransformer()
        val expr = "2 + 3 (7 * 3 + 10 / 5 / 2 - 2) - 3 + (1 - 2 - 562626)"
        val parser = Parser(expr)
        val ast = parser.buildAST()
        val ast2 = ast.accept(transformer)
        println(ast2)
    }
}