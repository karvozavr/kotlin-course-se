package ru.hse.spb

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Not enough arguments, please, provide a filename.")
    }

    val fileName = args[0]
    val file = Paths.get(fileName)
    val sourceCode: String = Files.lines(file).collect(Collectors.joining())
    val parser = Parser(sourceCode)

    try {
        val ast = parser.ast
        runInterpreter(ast)
    } catch (e: ParserError) {
        println("Parser error: \n" + e.message)
    } catch (e: InterpreterError) {
        println("Interpreter runtime error: \n" + e.message)
    }
}