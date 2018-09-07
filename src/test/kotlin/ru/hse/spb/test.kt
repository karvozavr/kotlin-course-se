package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestSource {

    private val test1 = "7 2\n1 5 6 2\n1 3\n3 2\n4 5\n3 7\n4 3\n4 6"
    private val test2 = "9 3\n3 2 1 6 5 9\n8 9\n3 2\n2 7\n3 4\n7 6\n4 5\n2 1\n2 8"

    @Test
    fun statementsTest1() {
        val solution = buildSolutionFromInput(Scanner(test1))
        assertEquals(6, solution.solve())
    }

    @Test
    fun statementsTest2() {
        val solution = buildSolutionFromInput(Scanner(test2))
        assertEquals(9, solution.solve())
    }
}