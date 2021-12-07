package day5

import java.io.File
import kotlin.math.max

fun main() {
    val input = parseVents(File("src/day5/input.txt"))

    println("Day Five")
    println("Part One: ${vent(input)}")
    println("Part Two: ${vent(input, true)}")
    println()
}

private fun vent(input: Triple<Int, Int, List<List<Int>>>, withDiagonals: Boolean = false): Int {
    val (x, y, lines) = input
    val grid = Array(x) { Array(y) { 0 } }
    lines.forEach { (x1, y1, x2, y2) ->
        when {
            x1 == x2 -> (minOf(y1, y2)..maxOf(y1, y2)).forEach { grid[x1][it]++ }
            y1 == y2 -> (minOf(x1, x2)..maxOf(x1, x2)).forEach { grid[it][y1]++ }
            withDiagonals -> diagonal(x1, y1, x2, y2, grid)
        }
    }
    return grid.flatten().count { it > 1 }
}

private fun diagonal(x1: Int, y1: Int, x2: Int, y2: Int, grid: Array<Array<Int>>) {
    var j = x2
    val left = x1 > x2
    val right = x1 < x2
    val down = y1 > y2
    val up = y1 < y2
    when {
        left && down -> while (j < x1) {
            for (k in y2..y1) grid[j++][k]++
            j++
        }
        left && up -> while (j < x1) {
            for (k in y2 downTo y1) grid[j++][k]++
            j++
        }
        right && down -> while (j >= x1) {
            for (k in y2..y1) grid[j--][k]++
            j--
        }
        right && up -> while (j >= x1) {
            for (k in y2 downTo y1) grid[j--][k]++
            j--
        }
    }
}

private fun parseVents(file: File): Triple<Int, Int, List<List<Int>>> {
    var dimX = 0
    var dimY = 0
    val lines = file.readLines()
        .map { line ->
            val (start, end) = line.split(" -> ")
            val (x1, y1) = start.split(",").map(String::toInt)
            val (x2, y2) = end.split(",").map(String::toInt)
            dimX = max(dimX, max(x1, x2))
            dimY = max(dimY, max(y1, y2))
            listOf(x1, y1, x2, y2)
        }
    return Triple(dimX + 1, dimY + 1, lines)
}