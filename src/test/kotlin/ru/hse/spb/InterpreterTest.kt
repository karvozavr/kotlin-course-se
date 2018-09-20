package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class InterpreterTest {

    private fun outputCollectingStd(stringBuilder: StringBuilder): Library =
            mutableMapOf("println" to { args -> stringBuilder.append(args.asSequence().joinToString(" ", postfix = "\n")); 0 })


    @Test(expected = InterpreterError::class)
    fun testError1() {
        val ast = Block(listOf(
                VariableDeclaration(Identifier("a"), Literal(23)),
                VariableDeclaration(Identifier("a"), Literal(24))
        ))
        runInterpreter(ast)
    }

    @Test(expected = InterpreterError::class)
    fun testError2() {
        val ast = Block(listOf(
                Identifier("a")
        ))
        runInterpreter(ast)
    }

    @Test
    fun testSimple() {
        val ast = Block(listOf(
                VariableDeclaration(Identifier("a"), Literal(23)),
                VariableDeclaration(Identifier("b"), Literal(24)),
                FunctionCall(Identifier("println"), Arguments(listOf(
                        BinaryExpression(Identifier("a"), Identifier("b"), BinaryExpression.Operation.PLUS)
                )))
        ))
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "47"
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testShadowing() {
        val ast = Block(listOf(
                VariableDeclaration(Identifier("a"), Literal(1)),
                If(Literal(1), Block(listOf(
                        VariableDeclaration(Identifier("a"), Literal(2)),
                        FunctionCall(Identifier("println"), Arguments(listOf(
                                Identifier("a")
                        ))),
                        Assignment(Identifier("a"), Literal(3)),
                        FunctionCall(Identifier("println"), Arguments(listOf(
                                Identifier("a")
                        )))
                ))),
                FunctionCall(Identifier("println"), Arguments(listOf(
                        Identifier("a")
                )))
        ))
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "2\n3\n1"
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testInterpreterHard1() {
        val ast = Block(listOf(
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
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = """
            1 1
            2 2
            3 3
            4 5
            5 8
        """.trimIndent()
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testInterpreterHard2() {
        val ast = Block(listOf(
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
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "42"
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testInterpreterHard3() {
        val ast = Block(listOf(
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
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "0"
        assertEquals(expected, outputCollector.toString().trim())
    }
}