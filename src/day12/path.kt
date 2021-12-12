package day12

import java.io.File

fun main() {
    val connections = parseConnections(File("src/day12/input.txt"))

    println("Day Twelve")
    println("Part One: ${paths(connections, "start", mutableSetOf())}")
    println("Part Two: ${paths2(connections, "start", mutableSetOf())}")
    println()
}

private fun paths(
    connections: Map<String, List<String>>, cave: String, seen: MutableSet<String>
): Int = when {
    cave == "end" -> 1
    cave == cave.lowercase() && cave in seen -> 0
    else -> {
        seen.add(cave)
        connections[cave]!!.sumOf { paths(connections, it, seen.toMutableSet()) }
    }
}

private fun paths2(
    connections: Map<String, List<String>>, cave: String, seen: MutableSet<String>, visited: String? = null
): Int = when {
    cave == "end" -> 1
    cave == "start" && seen.isNotEmpty() -> 0
    cave == cave.lowercase() && cave in seen && visited != null -> 0
    cave == cave.lowercase() && cave in seen && visited == null -> {
        seen.add(cave)
        connections[cave]!!.sumOf { paths2(connections, it, seen.toMutableSet(), cave) }
    }
    else -> {
        seen.add(cave)
        connections[cave]!!.sumOf { paths2(connections, it, seen.toMutableSet(), visited) }
    }
}

fun parseConnections(file: File): Map<String, List<String>> {
    val connections = mutableMapOf<String, MutableList<String>>()
    file.readLines().forEach { line ->
        val (source, destination) = line.split("-")
        connections.getOrPut(source) { mutableListOf() }.add(destination)
        connections.getOrPut(destination) { mutableListOf() }.add(source)
    }
    return connections
}