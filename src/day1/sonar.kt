package day1

import java.io.File

fun main() {
    val depths = File("src/day1/input.txt")
        .readLines()
        .map(String::toInt)

    println("Day One")
    println("Part One: ${sweep(depths, 1)}")
    println("Part Two: ${sweep(depths, 3)}")
    println()
}

private fun sweep(depths: List<Int>, windowSize: Int) =
    depths.asSequence().windowed(windowSize).map { it.sum() }
        .windowed(2).map { if (it[1] > it[0]) 1 else 0 }.sum()