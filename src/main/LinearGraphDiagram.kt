package lgdb

class LinearGraphDiagram private constructor(
    val left: LinearGraphDiagram?,
    val right: LinearGraphDiagram?,
    val type: LinearGraphDiagramType,
    private val value: String?
) {
    companion object {
        private const val EMPTY_VALUE = "_"
    }

    constructor(type: LinearGraphDiagramType, left: LinearGraphDiagram, right: LinearGraphDiagram) : this(
        left,
        right,
        type,
        null
    )

    constructor(value: String?, isNonterminal: Boolean = false) : this(
        null,
        null,
        if (isNonterminal) LinearGraphDiagramType.NEW_DIAGRAM else LinearGraphDiagramType.VALUE,
        if (value != EMPTY_VALUE) value else null
    )

    fun getValue(startIndex: Int = 0): Array<String> {
        if (type == LinearGraphDiagramType.VALUE) {
            return if (value != null) arrayOf(value) else emptyArray()
        }

        if (type == LinearGraphDiagramType.NEW_DIAGRAM) {
            return arrayOf("*", value ?: "")
        }

        val leftValue: Array<String>
        val rightValue: Array<String>

        return when (type) {
            LinearGraphDiagramType.AND -> {
                leftValue = left!!.getValue(startIndex)
                rightValue = right!!.getValue(startIndex + leftValue.size)
                leftValue.plus(rightValue)
            }

            LinearGraphDiagramType.OR -> {
                leftValue = left!!.getValue(startIndex + 2)
                rightValue = right!!.getValue(startIndex + leftValue.size + 4)
                if (leftValue.isEmpty()) {
                    val orValueLeftEmpty = mutableListOf("<", (startIndex + rightValue.size + 2).toString())
                    orValueLeftEmpty.addAll(rightValue)
                    orValueLeftEmpty.toTypedArray()
                }

                val orValue = mutableListOf("<")
                val rightValueIndex = startIndex + leftValue.size + 4
                orValue.add(rightValueIndex.toString())
                orValue.addAll(leftValue)
                orValue.add("↓")
                val orEndIndex = startIndex + leftValue.size + rightValue.size + 4
                orValue.add(orEndIndex.toString())
                orValue.addAll(rightValue)
                orValue.toTypedArray()
            }

            LinearGraphDiagramType.ITERATION -> {
                leftValue = left!!.getValue(startIndex)
                rightValue = right!!.getValue(startIndex + leftValue.size + 2)
                if (leftValue.isEmpty()) {
                    val iterationValueLeftEmpty = mutableListOf("<", (startIndex + rightValue.size + 4).toString())
                    iterationValueLeftEmpty.addAll(rightValue)
                    iterationValueLeftEmpty.add("↓")
                    iterationValueLeftEmpty.add(startIndex.toString())
                    iterationValueLeftEmpty.toTypedArray()
                }

                if (rightValue.isEmpty()) {
                    val iterationValueRightEmpty = mutableListOf<String>()
                    iterationValueRightEmpty.addAll(leftValue)
                    iterationValueRightEmpty.add("<")
                    iterationValueRightEmpty.add(startIndex.toString())
                    iterationValueRightEmpty.toTypedArray()
                }

                val iterationValue = mutableListOf<String>()
                iterationValue.addAll(leftValue)
                iterationValue.add("<")
                val iterationEndIndex = startIndex + leftValue.size + rightValue.size + 4
                iterationValue.add(iterationEndIndex.toString())
                iterationValue.addAll(rightValue)
                iterationValue.add("↓")
                iterationValue.add(startIndex.toString())
                iterationValue.toTypedArray()
            }

            else -> throw NotImplementedError()
        }
    }

    fun getResultValue(startIndex: Int = 0, endComment: String? = null): Array<String> {
        val result = getValue(startIndex).plus(".")

        if (endComment != null) {
            result[result.size - 1] += "($endComment)"
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinearGraphDiagram) return false

        return value == other.value &&
                left == other.left &&
                right == other.right &&
                type == other.type
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (left?.hashCode() ?: 0)
        result = 31 * result + (right?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString(): String {
        return getValue().joinToString(" ")
    }

    fun equals(left: LinearGraphDiagram?, right: LinearGraphDiagram?): Boolean {
        return left == right
    }
}