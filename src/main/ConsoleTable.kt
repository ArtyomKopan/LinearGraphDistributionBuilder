package lgdb

import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.math.max

object ConsoleTable {

    fun printTable(
        tableContent: List<String>,
        rowLength: Int,
        columnsHeaders: List<String>,
        rowsHeaders: List<String>
    ) {
        val nRows = ceil(tableContent.size.toDouble() / rowLength).toInt()
        val itemWidth = max(max(tableContent.maxOf { it.length }, columnsHeaders.maxOf { it.length }),
            rowsHeaders.maxOf { it.length })
        val horizontalLineLength = (rowLength + 2) * (itemWidth + 3) + 1

        require(columnsHeaders.size == rowLength && rowsHeaders.size == nRows) { "Не хватает заголовков!" }

        val horizontalLine = "_".repeat(horizontalLineLength)

        println(horizontalLine)

        val rows = mutableListOf<List<String>>()

        for (i in 0..<nRows) {
            val indexOfEndRow = min((i + 1) * rowLength, tableContent.size)
            val row = tableContent.subList(i * rowLength, indexOfEndRow).toMutableList()
            while (row.size < rowLength) {
                row.add(" ")
            }
            row.add(0, rowsHeaders[i])
            rows.add(row)
        }

        printRow(rowLength, listOf(" ").plus(columnsHeaders), itemWidth)
        println(horizontalLine)
        for (row in rows) {
            printRow(rowLength, row, itemWidth)
            println(horizontalLine)
        }
    }

    // rowLength -- число элементов в строке
    // rowContent -- элементы для печати
    // itemWidth -- ширина одной ячейки
    private fun printRow(rowLength: Int, rowContent: List<String>, itemWidth: Int) {
        require(rowContent.size == rowLength + 1) { "В списке недостаточно элементов для вывода!" }

        val items = mutableListOf<String>()
        for (item in rowContent) {
            // side == 1 => добавляем пробел справа;
            // side == 0 => добавляем пробел слева
            // (выравнивание по центру)
            var side = 1
            val itemBuilder = StringBuilder(item)
            while (itemBuilder.length < itemWidth) {
                side = if (side == 0) {
                    itemBuilder.insert(0, " ")
                    1
                } else {
                    itemBuilder.append(" ")
                    0
                }
            }
            items.add(itemBuilder.toString())
        }

        val row = StringBuilder()
        items.forEach { row.append("| $it ") }
        row.append("|")
        println(row.toString())
    }
}