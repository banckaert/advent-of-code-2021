package day2

import java.io.File

fun main() {
    val movements = File("src/day2/input.txt")
        .readLines()
        .map(::parseMovementLine)

    println("Day Two")
    println("Part One: ${dive(movements, ::move1)}")
    println("Part Two: ${dive(movements, ::move2)}")
    println()
}

data class Movement(val dir: String, val value: Int)
data class Position(var x: Int = 0, var y: Int = 0, var a: Int = 0) {
    fun course() = x * y
}

private fun parseMovementLine(line: String): Movement {
    val (dir, value) = line.split(" ")
    return Movement(dir, value.toInt())
}

private fun dive(movements: List<Movement>, move: (position: Position, movement: Movement) -> Unit): Int {
    val position = Position()
    movements.forEach { movement -> move(position, movement) }
    return position.course()
}

private fun move1(position: Position, movement: Movement) {
    when (movement.dir) {
        "forward" -> position.x += movement.value
        "up" -> position.y -= movement.value
        "down" -> position.y += movement.value
    }
}

private fun move2(position: Position, movement: Movement) {
    when (movement.dir) {
        "forward" -> {
            position.x += movement.value
            position.y += (position.a * movement.value)
        }
        "up" -> position.a -= movement.value
        "down" -> position.a += movement.value
    }
}