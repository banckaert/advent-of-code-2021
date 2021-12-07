package day4

import java.io.File
import kotlin.math.sqrt

fun main() {
    val (numbers, boards) = parseBoards(File("src/day5/test.txt"))

    println("Day Four")
    println("Part One: ${win(numbers, boards)}")
    println("Part Two: ${loose(numbers, boards)}")
    println()
}

class Cell(val number: Int, var isDrawn: Boolean = false)
class Board(private val cells: Matrix<Cell>) {
    fun mark(number: Int) = cells.get().forEach { cell -> if (cell.number == number) cell.isDrawn = true }
    fun won() = hasCompleteLines(cells.getRows()) || hasCompleteLines(cells.getColumns())
    private fun hasCompleteLines(lines: List<List<Cell>>) = lines.map { l -> l.all { c -> c.isDrawn } }.any { it }
    fun score() = cells.get().filter { cell -> !cell.isDrawn }.sumOf(Cell::number)
}

fun win(numbers: List<Int>, boards: List<Board>): Int {
    numbers.forEach { number ->
        boards.forEach { board ->
            board.mark(number)
            if (board.won()) {
                return board.score() * number
            }
        }
    }
    return 0
}

fun loose(numbers: List<Int>, boards: List<Board>): Int {
    boards.forEach { it.mark(numbers.first()) }
    return if (boards.size == 1 && boards.first().won())
        boards.first().score() * numbers.first()
    else loose(numbers.subList(1, numbers.size), boards.filter { !it.won() })
}

fun parseBoards(input: File): Pair<List<Int>, List<Board>> {
    val inputLines = input.readLines()
    val numbers = inputLines.first().split(",").map(String::toInt)
    val boards = inputLines.subList(1, inputLines.size)
        .filter { it.isNotEmpty() }
        .map { it.split(" ").filter(String::isNotBlank).map(String::toInt) }
        .chunked(5).map { Board(Matrix(it.flatten().map(::Cell))) }
    return Pair(numbers, boards)
}

class Matrix<T>(private val data: List<T>) {
    private var transposed: List<T>? = null
    private val dimension: Int = sqrt(data.size.toDouble()).toInt()

    fun get(row: Int, column: Int) = data[row * 5 + column]
    fun get() = data
    fun getRows() = data.chunked(dimension)
    fun getColumns() = transpose().chunked(dimension)

    @Suppress("UNCHECKED_CAST")
    private fun transpose(): List<T> {
        if (transposed == null) {
            val transpose: Array<T?> = arrayOfNulls<Any?>(data.size) as Array<T?>
            (0 until dimension).forEach { i ->
                (0 until dimension).forEach { j ->
                    transpose[i * dimension + j] = data[i + j * dimension]
                }
            }
            transposed = transpose.filterNotNull()
        }
        return transposed!!
    }
}