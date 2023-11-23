package lgdb


object ReversePolishNotation {
    private class ExpressionTreeNode(
        val weight: Int,
        val value: String
    ) {
        var leftNode: ExpressionTreeNode? = null
        var rightNode: ExpressionTreeNode? = null
    }

    private val SUPPORTED_OPERATORS = "(),;#"

    fun convert(expression: String): String {
        if (expression.isEmpty()) {
            throw IllegalArgumentException("The expression is empty")
        }

        val items = mutableListOf<String>()
        val expressionItems = splitExpression(expression)

        val rootNode = buildExpressionTree(expressionItems)
        traverseExpressionTree(items, rootNode)

        val builder = StringBuilder()
        (0..<items.size - 1).forEach { builder.append(items[it] + " ") }

        return if (items.size == 0)
            builder.toString()
        else builder.append(items.last).toString()
    }

    fun <T> calculate(rpnExpression: String, rpnCalculator: ReversePolishNotationCalculator<T>): T {
        val buffer = ArrayDeque<T>()
        val data = rpnExpression.split(" ").filter { it.isNotBlank() }

        for (currentValue in data) {
            if (currentValue.length == 1 && currentValue[0] in SUPPORTED_OPERATORS) {
                val secondValue = buffer.removeLast()
                val firstValue = buffer.removeLast()
                val result = rpnCalculator.calculate(firstValue, secondValue, currentValue[0])
                buffer.add(result)
            } else {
                buffer.add(rpnCalculator.convert(currentValue))
            }
        }

        return buffer.removeLast()
    }

    private fun splitExpression(expression: String): List<String> {
        val expressionItems = mutableListOf<String>()
        val currentValue = StringBuilder()

        for (ch in expression) {
            if (ch !in SUPPORTED_OPERATORS) {
                currentValue.append(ch)
            } else {
                if (currentValue.isNotEmpty()) {
                    expressionItems.add(currentValue.toString())
                    currentValue.clear()
                }
                expressionItems.add("$ch ")
            }
        }

        if (currentValue.isNotEmpty()) {
            expressionItems.add(currentValue.toString())
        }

        return expressionItems.toList()
    }

    private fun buildExpressionTree(expressionItems: List<String>): ExpressionTreeNode? {
        var currentWeight = 0
        val nodesStack = ArrayDeque<ExpressionTreeNode>()

        for (item in expressionItems) {
            when (item) {
                "(" -> currentWeight += 10
                ")" -> currentWeight -= 10
                else -> {
                    val weight = getWeight(currentWeight, item)
                    val node = ExpressionTreeNode(weight, item)

                    while (nodesStack.isNotEmpty() && node.weight <= nodesStack.last.weight) {
                        node.leftNode = nodesStack.removeLast()
                    }

                    if (nodesStack.isNotEmpty()) {
                        val lastNode = nodesStack.removeLast()
                        lastNode.rightNode = node
                        nodesStack.add(node)
                    }
                }
            }
        }

        return nodesStack.removeFirstOrNull()
    }

    private fun traverseExpressionTree(currentItems: MutableList<String>, currentNode: ExpressionTreeNode?) {
        currentNode ?: return
        traverseExpressionTree(currentItems, currentNode.leftNode)
        traverseExpressionTree(currentItems, currentNode.rightNode)
        currentItems.add(currentNode.value)
    }

    private fun getWeight(currentWeight: Int, value: String) = when (value) {
        "," -> currentWeight + 1
        "#" -> currentWeight + 2
        ";" -> currentWeight + 3
        else -> Int.MAX_VALUE
    }
}