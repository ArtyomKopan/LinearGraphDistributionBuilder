package lgdb

import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.math.ceil

fun main(args: Array<String>) {
    val grammarData: List<String>?

    when (args.size) {
        0 -> {
            println("Если Вы хотите ввести грамматику из консоли, то введите 0")
            println("Если Вы хотите ввести грамматику из файла, то введите 1")
            val mode = readln().toInt()
            grammarData = when (mode) {
                0 -> readGrammarFromConsole()
                1 -> {
                    print("Введите путь к файлу с грамматикой: ")
                    val pathToInputFile = readln()
                    readGrammarFromFile(pathToInputFile)
                }

                else -> throw IllegalArgumentException("Введено некорректное значение")
            }
        }

        1 -> grammarData = readGrammarFromFile(args[0])
        else -> {
            println("Некорректное количество аргументов программы!")
            return
        }
    }

    if (grammarData.isNullOrEmpty()) {
        return
    }

    val grammar = GrammarParser.parse(grammarData)

    val diagram = LinearGraphDiagramBuilder.build(grammar)

    println("Линейное представление грамматики: ")
    val nRows = ceil(diagram.size.toDouble() / 10).toInt()
    ConsoleTable.printTable(
        diagram,
        10,
        (0..9).map { it.toString() },
        (0..<nRows).map { it.toString() }
    )
    // diagram.forEach { print("$it ") }
}

fun readGrammarFromConsole(): List<String> {
    print("Введите количество правил в грамматике: ")
    val n = readln().toInt()
    println("Введите описание грамматики в следующем виде: ")
    println("<нетерминал 1> : <регулярное выражение 1>")
    println("<нетерминал 2> : <регулярное выражение 2>")
    println("...")
    println("<нетерминал n> : <регулярное выражение n>")

    val grammarData = mutableListOf<String>()

    for (i in 1..n) {
        val rule = readlnOrNull()
        rule ?: break
        grammarData.add(rule)
    }

    return grammarData.toList()
}

fun readGrammarFromFile(path: String): List<String>? =
    try {
        val grammarData = Files.readAllLines(Path(path))
        grammarData
    } catch (e: NoSuchFileException) {
        println("Файл не найден!")
        null
    }
