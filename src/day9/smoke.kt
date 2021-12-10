package day9


import java.io.File

fun main() {
    val heightmap = File("src/day9/input.txt").readLines()
        .map { line -> line.map(Character::getNumericValue) }

    println("Day Nine")
    println("Part One: ${part1(heightmap)}")
    println("Part Two: ${part2(heightmap)}")
    println()
}

private fun height(heightmap: List<List<Int>>, row: Int, column: Int) =
    if (row < 0 || column < 0 || row >= heightmap.size || column >= heightmap[0].size) Int.MAX_VALUE
    else heightmap[row][column]

private fun isLowPoint(heightmap: List<List<Int>>, row: Int, column: Int) =
    height(heightmap, row, column) < height(heightmap, row - 1, column) &&
            height(heightmap, row, column) < height(heightmap, row + 1, column) &&
            height(heightmap, row, column) < height(heightmap, row, column - 1) &&
            height(heightmap, row, column) < height(heightmap, row, column + 1)

fun part1(heightmap: List<List<Int>>): Int {
    var sum = 0;
    heightmap.indices.forEach { row ->
        heightmap[0].indices
            .filter { isLowPoint(heightmap, row, it) }
            .forEach { sum += heightmap[row][it] + 1 }
    }
    return sum
}

fun part2(heightmap: List<List<Int>>): Int {
    var basins = mutableListOf<Int>();
    heightmap.indices.forEach { row ->
        heightmap[0].indices
            .filter { isLowPoint(heightmap, row, it) }
            .forEach { basins.add(basin(heightmap, row, it)) }
    }
    basins.sortDescending()
    return basins[0] * basins[1] * basins[2]
}

private fun neighbours(heightmap: List<List<Int>>, row: Int, column: Int) =
    listOf(Pair(row - 1, column), Pair(row + 1, column), Pair(row, column - 1), Pair(row, column + 1))
        .filter { height(heightmap, it.first, it.second) < 9 }

private fun basin(heightmap: List<List<Int>>, row: Int, column: Int): Int {
    val queue = mutableListOf(Pair(row, column))
    val visited = mutableSetOf<Pair<Int, Int>>()
    while (queue.size > 0) {
        val current = queue.removeAt(0)
        if (visited.contains(current)) continue
        queue.addAll(neighbours(heightmap, current.first, current.second))
        visited.add(current)
    }
    return visited.size
}