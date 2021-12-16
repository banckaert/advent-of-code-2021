package day15

import java.io.File

fun main() {
    val grid = File("src/day15/input.txt").readLines()
        .map { row -> row.split("").filter { it.isNotBlank() }.map(String::toInt) }

    println("Day Fifteen")
    println("Part One: ${dijkstra(grid)}")
    println("Part Two: ${dijkstra(expand(grid))}")
    println()
}

private fun expand(grid: List<List<Int>>): List<List<Int>> {
    val expanded = Array(grid.size * 5) { IntArray(grid[0].size * 5) { Int.MAX_VALUE } }
    for (x in expanded.indices) {
        for (y in expanded[0].indices) {
            var newVal = grid[x % grid.size][y % grid[0].size]
            for (i in 0 until x / grid.size + y / grid[0].size) {
                newVal++
                if (newVal == 10) newVal = 1
            }
            expanded[x][y] = newVal
        }
    }
    return expanded.map { it.toList() }
}

private fun dijkstra(grid: List<List<Int>>): Int {
    var priorityQueue = mutableListOf(Triple(0, 0, 0))
    val costs = mutableMapOf<Pair<Int, Int>, Int>()
    while (true) {
        val (cost, x, y) = priorityQueue.removeAt(0)
        if (x == grid.size - 1 && y == grid[0].size - 1) return cost
        for (neighbour in neighbours4(Pair(x, y), grid)) {
            val newCost = cost + grid[neighbour.first][neighbour.second]
            if (neighbour in costs && costs[neighbour]!! <= newCost) continue
            costs[neighbour] = newCost
            priorityQueue.add(Triple(newCost, neighbour.first, neighbour.second))
        }
        priorityQueue.sortBy { it.first }
    }
}

private fun neighbours4(position: Pair<Int, Int>, grid: List<List<Int>>) = listOf(
    Pair(position.first - 1, position.second), Pair(position.first + 1, position.second),
    Pair(position.first, position.second - 1), Pair(position.first, position.second + 1)
).filter(grid::isValid)

private fun List<List<Int>>.isValid(position: Pair<Int, Int>) = -1 < position.first && position.first < this.size &&
        -1 < position.second && position.second < this[0].size