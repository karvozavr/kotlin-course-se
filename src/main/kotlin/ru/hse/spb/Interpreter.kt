package ru.hse.spb

typealias InterpreterCallable = (List<Int>) -> Int

data class InterpreterVariable(var value: Int)

typealias Library = MutableMap<String, InterpreterCallable>

class InterpreterError(message: String) : Exception(message)

/**
 * Interpreter evaluates given AST with context of given standard functions.
 */
fun runInterpreter(ast: Block, std: Library =
        mutableMapOf("println" to { args -> println(args.asSequence().joinToString(" ")); 0 })) {
    val standardScope = Scope(
            Scope.ScopeDict(std, mutableSetOf()),
            Scope.ScopeDict(mutableMapOf(), mutableSetOf())
    )

    EvaluationContext(standardScope).eval(ast)
}

/**
 * Evaluates AST in a context of given scope.
 */
class EvaluationContext(private val scope: Scope) {

    fun eval(block: Block): Int? {
        for (statement in block.statements) {
            val result = eval(statement)
            if (result != null) {
                return result
            }
        }
        return null
    }

    private fun eval(statement: Statement): Int? {
        when (statement) {
            is Function -> {
                val name = statement.identifier.name
                val closureScope = Scope(scope)
                val function: InterpreterCallable = { args: List<Int> -> statement.call(args, scope) }
                scope.functions.add(name, function)
                closureScope.functions.add(name, function)
            }

            is VariableDeclaration ->
                scope.variables.add(statement.identifier.name, InterpreterVariable(eval(statement.value)))

            is Expression ->
                eval(statement)

            is While ->
                while (eval(statement.condition) != 0) {
                    val result = EvaluationContext(Scope(scope)).eval(statement.body)
                    if (result != null) {
                        return result
                    }
                }

            is If ->
                return EvaluationContext(Scope(scope)).eval(if (eval(statement.condition) != 0) statement.ifTrue else statement.ifFalse)

            is Assignment ->
                scope.variables[statement.identifier.name].value = eval(statement.value)

            is Return ->
                return eval(statement.value)
        }

        return null
    }

    private fun eval(expression: Expression): Int {
        return when (expression) {
            is FunctionCall ->
                scope.functions[expression.identifier.name](expression.args.arguments.map(this::eval))

            is BinaryExpression ->
                expression.operation(eval(expression.leftArgument), eval(expression.rightArgument))

            is Identifier ->
                scope.variables[expression.name].value

            is Literal ->
                expression.value

            else ->
                throw InterpreterError("Unknown error.")
        }
    }
}

private fun Function.call(arguments: List<Int>, closureScope: Scope): Int {
    if (arguments.size != parameterNames.params.size) {
        throw InterpreterError("Function \'${identifier.name}\' has ${parameterNames.params.size} arguments. But ${arguments.size} arguments were given")
    }

    val callScope = Scope(closureScope)

    for ((param, arg) in parameterNames.params.zip(arguments)) {
        callScope.variables.add(param.name, InterpreterVariable(arg))
    }

    return EvaluationContext(callScope).eval(body) ?: 0
}

/**
 * Interpreter scope mapping symbols -> values & checking for runtime correctness
 */
class Scope(val functions: ScopeDict<InterpreterCallable>, val variables: ScopeDict<InterpreterVariable>) {

    constructor(parent: Scope) : this(ScopeDict(parent.functions), ScopeDict(parent.variables))

    class ScopeDict<T>(private val dict: MutableMap<String, T>, private val declaredInCurrentScope: MutableSet<String>) {

        constructor(parentDict: ScopeDict<T>) : this(HashMap(parentDict.dict), mutableSetOf())

        operator fun get(name: String): T =
                dict[name] ?: throw InterpreterError("Symbol \'$name\' not found.")

        fun add(name: String, value: T) {
            if (name in declaredInCurrentScope) {
                throw InterpreterError("Symbol \'$name\' is already declared in current scope.")
            }
            dict[name] = value
            declaredInCurrentScope.add(name)
        }
    }
}
