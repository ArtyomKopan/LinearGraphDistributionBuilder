package lgdb


object LinearGraphDiagramBuilder {
    fun build(grammar: Map<String, String>): List<String> {
        if (grammar.isEmpty()) {
            return emptyList()
        }

        val linearDiagrams = mutableListOf<List<String>>()
        val nonterminalStartIndices = mutableMapOf<String, Int>()

        var currentResultDiagramLength = 1

        for (rule in grammar) {
            val nonterminal = rule.key
            val expression = rule.value
            val rpnExp = ReversePolishNotation.convert(expression)
            val calculator = ReversePolishNotationCalculator(::convertNode, ::calculateNodes)
            val diagramObject = ReversePolishNotation.calculate(rpnExp, calculator)
            val linearDiagram = diagramObject.getResultValue(currentResultDiagramLength, nonterminal)
            linearDiagrams.add(linearDiagram.toList())
            nonterminalStartIndices[nonterminal] = currentResultDiagramLength
            currentResultDiagramLength += linearDiagram.size
        }

        val diagramsGluing = linearDiagrams.flatMap { it.asIterable() }
        val resultDiagram: Array<String> = arrayOf(":") + diagramsGluing.toTypedArray()

        return replaceNonterminalsToIndices(resultDiagram, nonterminalStartIndices).toList()
    }

    private fun calculateNodes(x: LinearGraphDiagram, y: LinearGraphDiagram, operation: Char): LinearGraphDiagram =
        when (operation) {
            ',' -> LinearGraphDiagram(LinearGraphDiagramType.AND, x, y)
            ';' -> LinearGraphDiagram(LinearGraphDiagramType.OR, x, y)
            '#' -> LinearGraphDiagram(LinearGraphDiagramType.LOOP, x, y)
            else -> throw IllegalArgumentException("Недопустимая операция: ${operation}")
        }

    private fun convertNode(value: String): LinearGraphDiagram {
        val isNonterminal = value.length == 1 && value == value.uppercase() && value != "_"
        return LinearGraphDiagram(value, isNonterminal)
    }

    private fun replaceNonterminalsToIndices(diagram: Array<String>, nonterminalStartIndices: Map<String, Int>): Array<String> {
        for (i in 0..<diagram.size) {
            if (diagram[i] == "*" && i < diagram.size - 1) {
                val nonterminal = diagram[i + 1]
                diagram[i + 1] = nonterminalStartIndices[nonterminal].toString()
            }
        }
        return diagram
    }
}