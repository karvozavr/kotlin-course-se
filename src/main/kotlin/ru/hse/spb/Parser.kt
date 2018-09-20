package ru.hse.spb

import org.antlr.v4.runtime.*
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

class ParserError(override var message: String) : Exception(message)

/**
 * Parser
 * source code -> interpreter AST
 */
class Parser(sourceCode: String) {
    private val lexer = ExpLexer(CharStreams.fromString(sourceCode))
    private val parser = ExpParser(BufferedTokenStream(lexer))

    init {
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
                throw ParserError("Parsing error at $line:$charPositionInLine.")
            }
        })

        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
                throw ParserError("Lexer error at $line:$charPositionInLine.")
            }
        })
    }

    val ast: Block by lazy {
        val transformer = ASTTransformer()
        val antlrAST = parser.file()
        antlrAST.accept(transformer) as Block
    }
}