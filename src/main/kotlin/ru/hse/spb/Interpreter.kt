package ru.hse.spb

class Interpreter {

    private val scope: Scope = Scope()
    private val returnValue: ReturnValue = ReturnValue()

    fun runInterpreter(program: Block) {
        visit(program)
    }

    fun visit(node: Block) {
        scope.openScope()
        for (statement in node.statements) {
            visit(statement)
        }
        scope.closeScope()
    }

    fun visit(node: Statement) {
        return when (node) {
            is Function -> visit(node)
            is VariableDeclaration -> visit(node)
            is While -> visit(node)
            is If -> visit(node)
            is Assignment -> visit(node)
            is Return -> visit(node)
            else -> throw IllegalStateException()
        }
    }

    fun visit(node: Function) {
        scope.declareFunction(node)
    }

    fun visit(node: VariableDeclaration) {
        scope.declareVariable(node.name.name, visit(node.value))
    }

    fun visit(node: While) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun visit(node: If) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun visit(node: Assignment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun visit(node: Return) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun visit(node: Expression): Int {
        return when (node) {
            is Identifier -> visit(node)
            is FunctionCall -> visit(node)
            is BinaryExpression -> visit(node)
            is Literal -> visit(node)
            else -> throw IllegalStateException()
        }
    }

    fun visit(node: Identifier): Int {
        return scope.lookupVariable(node.name)
                ?: throw InterpreterError("Unknown variable name \"${node.name}\".")
    }

    fun visit(node: FunctionCall): Int {
        val function: Function = scope.lookupFunction(node.name.name)
                ?: throw InterpreterError("Unknown variable name \"${node.name}\".")
        scope.openScope()
        for ((argument, name) in node.args.arguments.zip(function.parameterNames.params.map { p -> p.name })) {
            scope.declareVariable(name, visit(argument))
        }
        visit(function.body)
        scope.closeScope()
        TODO() // Break runtime on return
        return returnValue.get()
    }

    fun visit(node: BinaryExpression): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun visit(node: Literal): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class InterpreterError(message: String) : Exception(message)

    private class ReturnValue {
        private var value: Int = 0

        fun get(): Int {
            val result = value
            value = 0
            return result
        }

        fun set(newValue: Int) {
            value = newValue
        }
    }

    private class Scope {
        private val variables = ArrayList<HashMap<String, Int>>()
        private val functions = ArrayList<HashMap<String, Function>>()

        fun openScope() {
            variables.add(HashMap())
            functions.add(HashMap())
        }

        fun closeScope() {
            variables.dropLast(1)
            functions.dropLast(1)
        }

        fun lookupVariable(symbol: String): Int? {
            for (scope in variables.asReversed()) {
                if (symbol in scope) {
                    return scope[symbol]
                }
            }
            return null
        }

        fun lookupFunction(symbol: String): Function? {
            for (scope in functions.asReversed()) {
                if (symbol in scope) {
                    return scope[symbol]
                }
            }
            return null
        }

        fun declareFunction(function: Function) {
            val symbol = function.name.name
            if (symbol in functions.last()) {
                throw InterpreterError("Function \"$symbol\" already declared in current scope.")
            }
            functions.last()["ss"] = function
        }

        fun declareVariable(symbol: String, value: Int) {
            if (symbol in variables.last()) {
                throw InterpreterError("Variable \"$symbol\" already declared in current scope.")
            }
            variables.last()["ss"] = value
        }

        fun updateVariable(symbol: String, value: Int) {
            for (scope in variables.asReversed()) {
                if (symbol in scope) {
                    scope[symbol] = value
                    return
                }
            }
            throw InterpreterError("Unknown variable name \"$symbol\".")
        }
    }
}