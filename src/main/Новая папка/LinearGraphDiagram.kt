package lgdb

class LinearGraphDiagram private constructor(
    val left: LinearGraphDiagram?,
    val right: LinearGraphDiagram?,
    val type: LinearGraphDiagramType,
    private val value: String?
) {
    companion object {
        private const val EMPTY_VALUE = "_"

        fun createValue(value: String?, isNonterminal: Boolean = false): LinearGraphDiagram {
            val diagramType = if (isNonterminal) LinearGraphDiagramType.NEW_DIAGRAM else LinearGraphDiagramType.VALUE
            return LinearGraphDiagram(null, null, diagramType, if (value != EMPTY_VALUE) value else null)
        }
    }

    constructor(type: LinearGraphDiagramType, left: String, right: String) : this(
        LinearGraphDiagram.createValue(left),
        LinearGraphDiagram.createValue(right),
        type,
        null
    )

    constructor(type: LinearGraphDiagramType, left: String, right: LinearGraphDiagram) : this(
        LinearGraphDiagram.createValue(left),
        right,
        type,
        null
    )

    constructor(type: LinearGraphDiagramType, left: LinearGraphDiagram, right: String) : this(
        left,
        LinearGraphDiagram.createValue(right),
        type,
        null
    )

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
                orValue.add("|")
                val orEndIndex = startIndex + leftValue.size + rightValue.size + 4
                orValue.add(orEndIndex.toString())
                orValue.addAll(rightValue)
                orValue.toTypedArray()
            }

            LinearGraphDiagramType.LOOP -> {
                leftValue = left!!.getValue(startIndex)
                rightValue = right!!.getValue(startIndex + leftValue.size + 2)
                if (leftValue.isEmpty()) {
                    val loopValueLeftEmpty = mutableListOf("<", (startIndex + rightValue.size + 4).toString())
                    loopValueLeftEmpty.addAll(rightValue)
                    loopValueLeftEmpty.add("|")
                    loopValueLeftEmpty.add(startIndex.toString())
                    loopValueLeftEmpty.toTypedArray()
                }

                if (rightValue.isEmpty()) {
                    val loopValueRightEmpty = mutableListOf<String>()
                    loopValueRightEmpty.addAll(leftValue)
                    loopValueRightEmpty.add("<")
                    loopValueRightEmpty.add(startIndex.toString())
                    loopValueRightEmpty.toTypedArray()
                }

                val loopValue = mutableListOf<String>()
                loopValue.addAll(leftValue)
                loopValue.add("<")
                val loopEndIndex = startIndex + leftValue.size + rightValue.size + 4
                loopValue.add(loopEndIndex.toString())
                loopValue.addAll(rightValue)
                loopValue.add("|")
                loopValue.add(startIndex.toString())
                loopValue.toTypedArray()
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

    fun notEquals(left: LinearGraphDiagram?, right: LinearGraphDiagram?): Boolean {
        return left != right
    }
}