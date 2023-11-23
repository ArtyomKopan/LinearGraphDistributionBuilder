package lgdb

object GrammarParser {
    fun parse(data: List<String>): Map<String, String> {
        val grammar = mutableMapOf<String, String>()

        for (line in data) {
            val parts = line.split(":").map { it.trim() }
            if (parts.size != 2) {
                throw IllegalArgumentException("Некорректный формат ввода грамматики!")
            }

            val nonterminal = parts[0]
            val regex = if (parts[1].endsWith('.'))
                parts[1].substring(0, parts[1].length - 1)
            else parts[1]

            grammar[nonterminal] = regex
        }

        return grammar
    }
}