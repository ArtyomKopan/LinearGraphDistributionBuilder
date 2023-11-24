package lgdb

import java.util.Stack


object ReversePolishNotation {
    private class ExpressionTreeNode(
        val weight: Int,
        val value: String
    ) {
        var leftNode: ExpressionTreeNode? = null
        var rightNode: ExpressionTreeNode? = null

        fun printExpressionTree(spacesCount: Int = 0) {
            val startString = StringBuilder()
            (1..spacesCount).forEach { startString.append("_") }
            println(startString.toString() + this.toString())
            startString.append("__")
            leftNode ?: println(startString.toString())
            leftNode?.printExpressionTree(spacesCount + 2)
            rightNode ?: println(startString.toString())
            rightNode?.printExpressionTree(spacesCount + 2)
        }

        override fun toString() =
            "ExpressionTreeNode(" +
                    "weight=$weight, " +
                    "value='$value'" +
                    ")"
    }

    private const val SUPPORTED_OPERATORS = "(),;#"

    // TODO: этот метод работает неправильно, возвращает пустую строку
    fun convert(expression: String): String {
        if (expression.isEmpty()) {
            throw IllegalArgumentException("Выражение пусто")
        }

        var items = listOf<String>()
        val expressionItems = splitExpression(expression)

        // TODO: проблема в этих двух строках
        val rootNode = buildExpressionTree(expressionItems)
        rootNode?.printExpressionTree(0)
        println(items) // для отладки
        items = traverseExpressionTree(items, rootNode)
        println(items) // для отладки

        val builder = StringBuilder()
        (0..<items.size - 1).forEach { builder.append(items[it] + " ") }

        return if (items.isEmpty())
            builder.toString()
        else builder.append(items.last()).toString()
    }

    fun <T> calculate(rpnExpression: String, rpnCalculator: ReversePolishNotationCalculator<T>): T {
        val buffer = Stack<T>()
        val data = rpnExpression.split(" ").filter { it.isNotBlank() }

        for (currentValue in data) {
            if (currentValue.length == 1 && currentValue[0] in SUPPORTED_OPERATORS) {
                val secondValue = buffer.pop()
                val firstValue = buffer.pop()
                val result = rpnCalculator.calculate(firstValue, secondValue, currentValue[0])
                buffer.push(result)
            } else {
                buffer.push(rpnCalculator.convert(currentValue))
            }
        }

        return buffer.pop()
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
        val nodesStack = Stack<ExpressionTreeNode>()

        for (item in expressionItems) {
            if (item == "(") {
                currentWeight += 10
                continue
            }

            if (item == ")") {
                currentWeight -= 10
                continue
            }

            val weight = getWeight(currentWeight, item)
            val node = ExpressionTreeNode(weight, item)

            while (nodesStack.isNotEmpty() && node.weight <= nodesStack.peek().weight) {
                node.leftNode = nodesStack.pop()
            }

            if (nodesStack.isNotEmpty()) {
                nodesStack.peek().rightNode = node
            }

            nodesStack.push(node)
        }

        if (nodesStack.isEmpty()) {
            return null
        }

        var output = nodesStack.pop()

        while (nodesStack.isNotEmpty()) {
            output = nodesStack.pop()
        }

        return output
    }

    private fun traverseExpressionTree(currentItems: List<String>, currentNode: ExpressionTreeNode?): List<String> {
        if (currentNode == null) {
            return emptyList()
        }

        val resultItemsList = mutableListOf<String>()

        resultItemsList.addAll(traverseExpressionTree(currentItems, currentNode.leftNode))
        resultItemsList.addAll(traverseExpressionTree(currentItems, currentNode.rightNode))
        resultItemsList.add(currentNode.value)

        return resultItemsList.toList()
    }

    private fun getWeight(currentWeight: Int, value: String) = when (value) {
        "," -> currentWeight + 1
        "#" -> currentWeight + 2
        ";" -> currentWeight + 3
        else -> Int.MAX_VALUE
    }
}