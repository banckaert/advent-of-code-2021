package day14

import java.io.File

fun main() {
    val instructions = File("src/day14/input.txt").readText()
        .split("${System.lineSeparator()}${System.lineSeparator()}")
    val template = instructions.first()
    val rules = instructions.last().split(System.lineSeparator()).associate {
        val (left, right) = it.split(" -> ")
        left to right
    }

    println("Day Twelve")
    println("Part One: ${applyInstructions(template, rules, 10)}")
    println("Part Two: ${applyInstructions(template, rules, 40)}")
    println()
}

fun applyInstructions(template: String, rules: Map<String, String>, steps: Int): Long {
    var pairs = buildMap { (1 until template.length).forEach { addTo("${template[it - 1]}${template[it]}", 1) } }

    repeat(steps) {
        pairs = buildMap {
            pairs.forEach { (k, v) ->
                addTo("${k.first()}${rules[k]}", v)
                addTo("${rules[k]}${k.last()}", v)
            }
        }
    }

    val occurrences = buildMap {
        pairs.forEach { (k, v) -> addTo(k.first(), v) }
        addTo(template.last(), 1)
    }

    return occurrences.maxOf { it.value } - occurrences.minOf { it.value }
}

private fun <K> MutableMap<K, Long>.addTo(key: K, value: Long) {
    this[key] = (this[key] ?: 0L) + value
}