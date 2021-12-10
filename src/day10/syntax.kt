package day10

import java.io.File

fun main() {
    val lines = File("src/day10/input.txt").readLines()
        .map { it.toCharArray() }

    println("Day Ten")
    println("Part One: ${corrupted(lines)}")
    println("Part Two: ${incomplete(lines)}")
    println()
}

private fun corrupted(lines: List<CharArray>) = lines.map { faulty(it) }.sumOf { it.second }
private fun incomplete(lines: List<CharArray>): Long {
    val incomplete = lines.map { score(it) }.filter { it != 0L }.sorted()
    return incomplete[incomplete.size / 2]
}

private val MAP = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
private val ERROR = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val SCORE = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

private fun faulty(line: CharArray): Pair<List<Char>, Int> {
    val stack = mutableListOf<Char>()
    line.forEach { char ->
        if (char in MAP) stack.add(char)
        else if (char != MAP[stack.removeAt(stack.size - 1)]) return Pair(stack, ERROR[char]!!)
    }
    return Pair(stack, 0)
}

private fun score(line: CharArray): Long {
    val faulty = faulty(line)
    return if (faulty.second == 0) {
        var score: Long = 0
        faulty.first.reversed().forEach { score = (5 * score + SCORE[it]!!) }
        score
    } else 0L
}