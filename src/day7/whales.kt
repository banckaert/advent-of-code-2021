package day7

import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val positions = File("src/day7/input.txt")
        .readLines().first().split(",").map(String::toInt)

    println("Day Seven")
    println("Part One: ${align(positions) { distance -> distance }}")
    val start = System.currentTimeMillis()
    println("Part Two: ${align(positions) { distance -> (1..distance).sum() }}")
    println(System.currentTimeMillis() - start)
    println()
}

private fun align(positions: List<Int>, fuelConsumption: (Int) -> Int): Int {
    val max = positions.maxOf { it }
    var minFuel: Int = Int.MAX_VALUE
    for (i in 0..max) minFuel = min(positions.sumOf { fuelConsumption(abs(it - i)) }, minFuel)
    return minFuel
}