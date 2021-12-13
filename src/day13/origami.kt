package day13

import java.io.File
import java.lang.System.lineSeparator

fun main() {
    val instructions = File("src/day13/input.txt").readText()
        .split("${lineSeparator()}${lineSeparator()}")
        .let { Instructions(it.first().toPoints(), it.last().toFolds()) }

    println("Day Twelve")
    println("Part One: ${follow(instructions, 1).size}")
    println("Part Two: ${follow(instructions).print()}")
    println()
}

private fun follow(instructions: Instructions, noInstructions: Int = instructions.folds.size) = instructions.let {
    it.folds.take(noInstructions).fold(it.points, Set<Point>::apply)
}

class Instructions(val points: Set<Point>, val folds: List<Fold>)

enum class Direction { X, Y }

data class Point(val x: Int, val y: Int) {
    fun fold(fold: Fold): Point = when (fold.direction) {
        Direction.X -> {
            if (this.x > fold.distance) Point(fold.distance - (this.x - fold.distance), this.y)
            else this
        }
        Direction.Y -> this.invert().fold(fold.invert()).invert()
    }

    private fun invert() = Point(y, x)
}

data class Fold(val direction: Direction, val distance: Int) {
    fun invert() = Fold(if (direction == Direction.X) Direction.Y else Direction.X, distance)
}

private fun Set<Point>.apply(fold: Fold) = this.fold(emptySet<Point>()) { points, point -> points + point.fold(fold) }
private fun Set<Point>.print() = StringBuilder(lineSeparator())
    .append((0..this.maxOf { it.y }).joinToString(lineSeparator()) { y ->
        (0..this.maxOf { it.x }).joinToString("") { x ->
            if (this.contains(Point(x, y))) "█" else " "
        }
    })
    .append(lineSeparator()).toString()

private fun String.toPoints() = this.split(lineSeparator())
    .map { it.split(',') }
    .map { Point(it.first().toInt(), it.last().toInt()) }
    .toSet()

private fun String.toFolds() = this.split(lineSeparator())
    .map { it.split('=') }
    .map { Fold(Direction.valueOf(it.first().last().uppercase()), it.last().toInt()) }