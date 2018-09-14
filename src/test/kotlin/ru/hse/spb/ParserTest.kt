package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class ParserTest {

    @Test
    fun testExpressionSimple1() {
        val expr = "2 - 3"
        val parser = Parser(expr)
        val ast = parser.buildAST()
        println(parser.buildAST())
    }
}