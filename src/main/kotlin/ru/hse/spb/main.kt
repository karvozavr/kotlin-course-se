package ru.hse.spb

fun getGreeting(): String {
    val words = mutableListOf<String>()
    words.add("Hello,")

    words.add("world!")

    return words.joinToString(separator = " ")
}

fun main(args: Array<String>) {
    val expr = """
            fun foo(n) {
                fun bar(m) {
                    return m + n
                }

                return bar(1)
            }

            println(foo(41)) // prints 42
        """
    val ast = Parser(expr).ast
    runInterpreter(ast)
}