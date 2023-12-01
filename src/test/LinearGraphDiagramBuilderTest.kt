package lgdb

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LinearGraphDiagramBuilderTest {

    @Test
    fun testEmptyGrammar() {
        val grammar = emptyMap<String, String>()
        val result = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testSimpleGrammar() {
        val grammar = mapOf("S" to "a,b,c")
        val result = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(listOf(":", "a", "b", "c", ".(S)"), result)
    }

    @Test
    fun TestNonterminals() {
        val grammar = mapOf("S" to "a,b,C", "C" to "a#x")
        val result = LinearGraphDiagramBuilder.build(grammar)
        assertEquals(listOf(":", "a", "b", "*", "6", ".(S)", "a", "<", "12", "x", "↓", "6", ".(C)"), result)
    }
}