package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class InterpreterTest {

    private fun collectingOutput(stringBuilder: StringBuilder): Library {
        return mutableMapOf("println" to { args -> stringBuilder.append(args.asSequence().joinToString(" ", postfix = "\n")); 0 })
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
        runInterpreter(ast, collectingOutput(outputCollector))
        val expected = """
            1 1
            2 2
            3 3
            4 5
            5 8

        """.trimIndent()
        assertEquals(expected, outputCollector.toString())
    }

    @Test
    fun testInterpreterHard() {
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
        runInterpreter(ast, collectingOutput(outputCollector))
        val expected = """
            1 1
            2 2
            3 3
            4 5
            5 8

        """.trimIndent()
        assertEquals(expected, outputCollector.toString())
    }
}