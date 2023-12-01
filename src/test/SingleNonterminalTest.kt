package lgdb

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SingleNonterminalTest {
    @Test
    fun test1() {
        val grammar = mapOf("S" to "a,b#c")

        val expected = arrayOf(":", "a", "b", "<", "8", "c", "↓", "2", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }

    @Test
    fun test2() {
        val grammar = mapOf("S" to "d,(a#b);c")

        val expected = arrayOf(":", "d", "<", "12", "a", "<", "10", "b", "↓", "4", "↓", "13", "c", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }

    @Test
    fun test3() {
        val grammar = mapOf("S" to "a#b#c#d,a")

        val expected = arrayOf(":", "a", "<", "7", "b", "↓", "1", "<", "12", "c", "↓", "1", "<", "17", "d",
            "↓", "1", "a", ".(S)")
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }
}