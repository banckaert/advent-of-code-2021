package day6

import java.io.File

fun main() {
    val population = File("src/day6/input.txt")
        .readLines().first().split(",").map(String::toInt)

    println("Day Six")
    println("Part One: ${procreate(population, 80)}")
    println("Part Two: ${procreate(population, 256)}")
    println()
}

private fun procreate(population: List<Int>, noGenerations: Int): Long {
    val generations = populate(population)
    repeat(noGenerations) { crossover(generations) }
    return generations.sum()
}

private fun populate(population: List<Int>): LongArray {
    val generations = LongArray(9) { 0 }
    for (fish in population) generations[fish]++
    return generations
}

private fun crossover(generations: LongArray) {
    val temp = generations[0]
    for (i in 0..7) generations[i] = generations[i + 1]
    generations[8] = temp
    generations[6] += temp
}