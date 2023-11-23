package lgdb

import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.math.ceil
import kotlin.math.max

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
}

fun readGrammarFromConsole(): List<String>? {
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
    } catch (e: FileNotFoundException) {
        println("Файл не найден!")
        null
    }

fun printTable(diagram: List<String>) {
    val rowsCount = ceil(diagram.size.toFloat() / 10).toInt()
    var cnt = 0

    val headers = (0..9).toList()
    val data = mutableListOf<MutableList<String>>()

    for (i in 1..rowsCount) {
        data.add(mutableListOf())
        if (i != rowsCount) {
            for (j in 1..10) {
                data[i][j] = diagram[cnt++]
            }
        } else {
            while (cnt < diagram.size) {
                data[i].add(diagram[cnt++])
            }
            for (j in 1..(10 - diagram.size % 10)) {
                data[i].add(" ")
            }
        }
    }

    printRow(headers)

    println("-".repeat(max(diagram.size.toString().length,
        diagram.maxOf { it.length }) + 3
    )
    )

    for (row in data) {
        printRow(row)
    }
}

fun printRow(row: List<Any>) {
    for (item in row) {
        print("| ${item.toString().padEnd(15)} ")
    }
    println("|")
}