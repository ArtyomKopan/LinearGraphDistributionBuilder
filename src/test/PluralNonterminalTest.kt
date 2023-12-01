package lgdb

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PluralNonterminalTest {

    @Test
    fun test1() {
        val grammar = mapOf(
            "S" to "(a,(a,S);a,a);(a,B,a)",
            "B" to "a"
        )

        val expected = arrayOf(
            ":", "<", "15", "a", "<", "11", "a", "*", "1", "↓", "12", "a", "a", "↓", "19", "a",
            "*", "20", "a", ".(S)", "a", ".(B)"
        )
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }

    @Test
    fun test2() {
        val grammar = mapOf(
            "S" to "(a,(a,S);a,a);(a,B,a),f,g",
            "B" to "f;g"
        )

        val expected = arrayOf(
            ":", "<", "15", "a", "<", "11", "a", "*", "1", "↓", "12", "a", "a", "↓", "19", "a",
            "*", "22", "a", "f", "g", ".(S)", "<", "27", "f", "↓", "28", "g", ".(B)"
        )
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }

    @Test
    fun test3() {
        val grammar = mapOf(
            "S" to "(A,Q);(B,b)",
            "B" to "A,Q",
            "A" to "a",
            "Q" to "a,(Q;q)"
        )

        val expected = arrayOf(
            ":", "<", "9", "*", "18", "*", "20", "↓", "12", "*", "13", "b", ".(S)", "*", "18", "*",
            "20", ".(B)", "a", ".(A)", "a", "<", "27", "*", "20", "↓", "28", "q", ".(Q)"
        )
        val actual = LinearGraphDiagramBuilder.build(grammar)
        Assertions.assertEquals(expected.toList(), actual)
    }
}