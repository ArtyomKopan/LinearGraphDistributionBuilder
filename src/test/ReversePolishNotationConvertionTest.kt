package lgdb

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ReversePolishNotationConvertionTest {

    @Test
    fun testSimpleConvertion() {
        val expression = "a,b,c"
        val result = ReversePolishNotation.convert(expression)
        assertEquals("a b ,  c , ", result)
    }

    @Test
    fun testParenthesesConvertion() {
        val expression = "a,(b;c)"
        val result = ReversePolishNotation.convert(expression)
        assertEquals("a b c ;  , ", result)
    }

    @Test
    fun testMultipleOperatorsConvertion() {
        val expression = "(a,b#c),d;(e#f,g,h)"
        val result = ReversePolishNotation.convert(expression)
        assertEquals("a b c #  ,  d e f #  g ,  h ,  ;  , ", result)
    }
}