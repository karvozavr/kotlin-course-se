package ru.hse.spb

import java.util.*
import kotlin.collections.ArrayList

/**
 * Solution class for codeforces 701E.
 *
 * @param adjacencyList adjacency list of the tree
 * @param univ bool mask: is i'th vertex a university
 * @param univNumber amount of universities
 */
class Solution(private val adjacencyList: Array<ArrayList<Int>>,
               private val univ: Array<Boolean>,
               private val univNumber: Int) {

    private val vertexNumber = adjacencyList.size
    private val subtreeUnivNumber = Array(vertexNumber) { _ -> 0 }
    private val used = Array(vertexNumber) { _ -> false }

    /**
     * Finds answer to problem.
     *
     * @return answer to problem
     */
    fun solve(): Long {
        dfs(0)

        var answer: Long = 0
        for (vertex in 0 until vertexNumber) {
            answer += Math.min(subtreeUnivNumber[vertex], univNumber - subtreeUnivNumber[vertex])
        }

        return answer
    }

    /**
     * Depth-first search calculating number of universities in a subtree of each vertex.
     *
     * @param vertex vertex to start dfs from
     */
    private fun dfs(vertex: Int) {
        subtreeUnivNumber[vertex] = if (univ[vertex]) 1 else 0
        used[vertex] = true

        for (neighbour in adjacencyList[vertex]) {
            if (!used[neighbour]) {
                dfs(neighbour)
                subtreeUnivNumber[vertex] += subtreeUnivNumber[neighbour]
            }
        }
    }
}

/**
 * Builds {@Link Solution} object from given input.
 *
 * @param input input source
 */
fun buildSolutionFromInput(input: Scanner): Solution {
    val n = input.nextInt()
    val k = input.nextInt()

    val univ = Array(n) { _ -> false }
    val adjacencyList = Array<ArrayList<Int>>(n) { _ -> ArrayList() }

    for (i in 0 until 2 * k) {
        univ[input.nextInt() - 1] = true
    }

    for (i in 0 until n - 1) {
        val v = input.nextInt() - 1
        val u = input.nextInt() - 1
        adjacencyList[v].add(u)
        adjacencyList[u].add(v)
    }

    return Solution(adjacencyList, univ, 2 * k)
}

fun main(args: Array<String>) {
    val solution = buildSolutionFromInput(Scanner(System.`in`))
    println(solution.solve())
}