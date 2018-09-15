package ru.hse.spb

import org.junit.Test
import org.junit.Assert.*

class ParserTest {

    @Test(expected = ParserError::class)
    fun testParserError1() {
        val expr = "a = 2 - "
        val parser = Parser(expr)
        val ast = parser.ast
    }

    @Test(expected = ParserError::class)
    fun testParserError2() {
        val expr = "++"
        val parser = Parser(expr)
        val ast = parser.ast
    }

    @Test
    fun testParserAssignment() {
        val expr = "a = 2"
        val parser = Parser(expr)
        val ast: Block = parser.ast
        val assignment = ast.statements[0]
        assertEquals(Assignment::class, assignment::class)
    }

    @Test
    fun testParserBlock() {
        val expr = "a = 2 b = 3 \n a = 5 * 4 - 1"
        val parser = Parser(expr)
        val ast = parser.ast
        assertEquals(Block::class, ast::class)
    }
}