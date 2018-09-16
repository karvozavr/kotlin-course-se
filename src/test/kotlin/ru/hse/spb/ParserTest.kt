package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun testParserLiteral() {
        val expr = "10"
        val parser = Parser(expr)
        val ast: Block = parser.ast
        val expectedAst = Block(listOf(Literal(10)))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserLiteralZero() {
        val expr = "0"
        val parser = Parser(expr)
        val ast: Block = parser.ast
        val expectedAst = Block(listOf(Literal(0)))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserAssignment() {
        val expr = "a = 2"
        val parser = Parser(expr)
        val ast: Block = parser.ast
        val expectedAst = Block(listOf(Assignment(Identifier("a"), Literal(2))))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserBlock() {
        val expr = "a = 2 b = 3 \n a = 5 * 4 - 1"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(
                Assignment(Identifier("a"), Literal(2)),
                Assignment(Identifier("b"), Literal(3)),
                Assignment(
                        Identifier("a"),
                        BinaryExpression(
                                BinaryExpression(
                                        Literal(5),
                                        Literal(4),
                                        BinaryExpression.Operation.MUL
                                ),
                                Literal(1),
                                BinaryExpression.Operation.MINUS
                        )
                )
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserVar() {
        val expr = "var a var b = 24"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(
                VariableDeclaration(Identifier("a"), Literal(0)),
                VariableDeclaration(Identifier("b"), Literal(24))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserFunction() {
        val expr = "fun foo(a, b, c, d) { var b = 24 }"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(Function(
                Identifier("foo"),
                ParameterNames(listOf(
                        Identifier("a"),
                        Identifier("b"),
                        Identifier("c"),
                        Identifier("d")
                )),
                Block(listOf(VariableDeclaration(Identifier("b"), Literal(24))))
        )))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserFunctionWithoutParams() {
        val expr = "fun foo() {  }"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(Function(
                Identifier("foo"),
                ParameterNames(emptyList()),
                Block(emptyList())
        )))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserWhile() {
        val expr = "while (a == b) { var b = 24 }"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(While(
                BinaryExpression(
                        Identifier("a"),
                        Identifier("b"),
                        BinaryExpression.Operation.EQ
                ),
                Block(listOf(VariableDeclaration(Identifier("b"), Literal(24))))
        )))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserComments() {
        val expr = "//comment \n while (a == b) // some comment\n  { var b = 24 }"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(While(
                BinaryExpression(
                        Identifier("a"),
                        Identifier("b"),
                        BinaryExpression.Operation.EQ
                ),
                Block(listOf(VariableDeclaration(Identifier("b"), Literal(24))))
        )))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserFunctionCall() {
        val expr = "foo(bar(23))"
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(FunctionCall(
                Identifier("foo"),
                Arguments(listOf(FunctionCall(
                        Identifier("bar"),
                        Arguments(listOf(Literal(23)))
                )))
        )))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserComplex1() {
        val expr = """
            var a = 10
            var b = 20
            if (a > b) {
              println(1)
            } else {
              println(0)
            }
        """
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(
                VariableDeclaration(Identifier("a"), Literal(10)),
                VariableDeclaration(Identifier("b"), Literal(20)),
                If(
                        BinaryExpression(Identifier("a"), Identifier("b"), BinaryExpression.Operation.GR),
                        Block(listOf(
                                FunctionCall(Identifier("println"), Arguments(listOf(Literal(1))))
                        )),
                        Block(listOf(
                                FunctionCall(Identifier("println"), Arguments(listOf(Literal(0))))
                        ))
                )
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserComplex2() {
        val expr = """
            fun fib(n) {
                if (n <= 1) {
                    return 1
                }
                return fib(n - 1) + fib(n - 2)
            }

            var i = 1
            while (i <= 5) {
                println(i, fib(i))
                i = i + 1
            }
        """
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(
                Function(
                        Identifier("fib"),
                        ParameterNames(listOf(Identifier("n"))),
                        Block(listOf(
                                If(
                                        BinaryExpression(
                                                Identifier("n"),
                                                Literal(1),
                                                BinaryExpression.Operation.LEQ
                                        ),
                                        Block(listOf(
                                                Return(Literal(1))
                                        ))
                                ),
                                Return(
                                        BinaryExpression(
                                                FunctionCall(
                                                        Identifier("fib"),
                                                        Arguments(listOf(
                                                                BinaryExpression(
                                                                        Identifier("n"),
                                                                        Literal(1),
                                                                        BinaryExpression.Operation.MINUS
                                                                )
                                                        ))
                                                ),
                                                FunctionCall(
                                                        Identifier("fib"),
                                                        Arguments(listOf(
                                                                BinaryExpression(
                                                                        Identifier("n"),
                                                                        Literal(2),
                                                                        BinaryExpression.Operation.MINUS
                                                                )
                                                        ))
                                                ),
                                                BinaryExpression.Operation.PLUS
                                        )
                                )
                        ))
                ),
                VariableDeclaration(Identifier("i"), Literal(1)),
                While(
                        BinaryExpression(Identifier("i"), Literal(5), BinaryExpression.Operation.LEQ),
                        Block(listOf(
                                FunctionCall(
                                        Identifier("println"),
                                        Arguments(listOf(
                                                Identifier("i"),
                                                FunctionCall(Identifier("fib"), Arguments(listOf(Identifier("i"))))
                                        ))
                                ),
                                Assignment(
                                        Identifier("i"),
                                        BinaryExpression(Identifier("i"), Literal(1), BinaryExpression.Operation.PLUS)
                                )
                        ))
                )
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun testParserComplex3() {
        val expr = """
            fun foo(n) {
                fun bar(m) {
                    return m + n
                }

                return bar(1)
            }

            println(foo(41)) // prints 42

        """
        val parser = Parser(expr)
        val ast = parser.ast
        val expectedAst = Block(listOf(
                Function(
                        Identifier("foo"),
                        ParameterNames(listOf(Identifier(("n")))),
                        Block(listOf(
                                Function(
                                        Identifier("bar"),
                                        ParameterNames(listOf(Identifier(("m")))),
                                        Block(listOf(
                                                Return(
                                                        BinaryExpression(
                                                                Identifier("m"),
                                                                Identifier("n"),
                                                                BinaryExpression.Operation.PLUS
                                                        )
                                                )
                                        ))
                                ),
                                Return(
                                        FunctionCall(
                                                Identifier("bar"),
                                                Arguments(listOf(Literal(1)))
                                        )
                                )
                        ))
                ),
                FunctionCall(
                        Identifier("println"),
                        Arguments(listOf(
                                FunctionCall(
                                        Identifier("foo"),
                                        Arguments(listOf(
                                                Literal(41)
                                        ))
                                )
                        ))
                )
        ))
        assertEquals(expectedAst, ast)
    }
}