package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

class Parser(sourceCode: String) {
    private val parser: ExpParser = ExpParser(BufferedTokenStream(ExpLexer(CharStreams.fromString(sourceCode))))

    fun buildAST(): ExpParser.EvalContext  {
        return parser.eval()
    }
}