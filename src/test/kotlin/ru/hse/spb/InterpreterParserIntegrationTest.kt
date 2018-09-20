package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class InterpreterParserIntegrationTest {

    private fun outputCollectingStd(stringBuilder: StringBuilder): Library =
            mutableMapOf("println" to { args -> stringBuilder.append(args.asSequence().joinToString(" ", postfix = "\n")); 0 })

    @Test
    fun testIntegration1() {
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
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "0"
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testIntegration2() {
        val expr =  """
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
    fun testIntegration3() {
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
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = "42"
        assertEquals(expected, outputCollector.toString().trim())
    }

    @Test
    fun testIntegration4() {
        val expr = """
            fun checkForPrime(n) {
                var i = 2
                while (i * i <= n) {
                    if (n % i == 0) {
                        return 0
                    }
                    i = i + 1
                }
                return 1
            }

            var i = 2
            while (i < 12) {
                println(i, checkForPrime(i))
                i = i + 1
            }
        """
        val parser = Parser(expr)
        val ast = parser.ast
        val outputCollector = StringBuilder()
        runInterpreter(ast, outputCollectingStd(outputCollector))
        val expected = """
            2 1
            3 1
            4 0
            5 1
            6 0
            7 1
            8 0
            9 0
            10 0
            11 1
        """.trimIndent()
        assertEquals(expected, outputCollector.toString().trim())
    }
}