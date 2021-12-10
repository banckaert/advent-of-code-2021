package day8

import java.io.File

fun main() {
    val notes = File("src/day8/input.txt").readLines().map { line ->
        val entry = line.split(" | ")
        entry[0].split(" ") to entry[1].split(" ")
    }

    println("Day Eight")
    println("Part One: ${search1(notes.map { it.second })}")
    println("Part Two: ${search2(notes)}")
    println()
}

private fun search1(output: List<List<String>>) = output.flatten().count { it.length in arrayOf(2, 3, 4, 7) }
private fun search2(notes: List<Pair<List<String>, List<String>>>) = notes.sumOf { decode(it) }

//   111
//  2   3
//  2   3
//   444
//  5   6
//  5   6
//   777
val digits = mapOf(
    "36" to 1,
    "13457" to 2,
    "13467" to 3,
    "2346" to 4,
    "12467" to 5,
    "124567" to 6,
    "136" to 7,
    "1234567" to 8,
    "123467" to 9,
    "123567" to 0,
)

private fun generatePermutations(input: List<Char> = ('a'..'g').toList(), base: String = ""): List<String> =
    if (input.isEmpty()) listOf(base)
    else input.map { generatePermutations(input.minus(it), "$base$it") }.flatten()

private val permutations = generatePermutations()

private fun translate(signal: String, mapping: String) = signal.map { mapping.indexOf(it) + 1 }.sorted().joinToString("")

private fun decode(pair: Pair<List<String>, List<String>>): Int {
    val entry = pair.first + pair.second
    var mappings = permutations
    entry.forEach { signal -> mappings = mappings.filter { mapping -> translate(signal, mapping) in digits } }
    return pair.second
        .map { translate(it, mappings[0]) }
        .map { digits[it]!! }
        .reduce { acc: Int, i: Int -> acc * 10 + i }
}