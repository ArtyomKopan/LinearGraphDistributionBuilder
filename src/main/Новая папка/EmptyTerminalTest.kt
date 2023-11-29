package lgdb

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class EmptyTerminalTests {

    @Test
    fun test1() {
        val grammar = mapOf("S" to "b,a#_")

        val expected = arrayOf(":", "b", "a", "<", "7", "|", "2", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(expected.toList(), actual)
    }

    @Test
    fun test2() {
        val grammar = mapOf("S" to "b,_#a")

        val expected = arrayOf(":", "b", "<", "7", "a", "|", "2", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(expected.toList(), actual)
    }

    @Test
    fun test3() {
        val grammar = mapOf("S" to "b,_;a")

        val expected = arrayOf(":", "b", "<", "6", "|", "7", "a", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(expected.toList(), actual)
    }
}