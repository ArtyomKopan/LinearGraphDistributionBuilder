package lgdb


class ReversePolishNotationCalculator<T>(
    private val converter: (String) -> T,
    private val calculator: (T, T, Char) -> T
) {
    fun convert(value: String): T = converter(value)

    fun calculate(firstValue: T, secondValue: T, operation: Char): T =
        calculator(firstValue, secondValue, operation)
}