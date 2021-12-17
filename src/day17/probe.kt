package day17

import kotlin.math.abs
import kotlin.math.max

fun main() {
    val targetArea = TargetArea((57..116), (-198..-148))

    println("Day Seventeen")
    val (highest, unique) = launch(targetArea)
    println("Part One: $highest")
    println("Part One: $unique")
    println()
}

class TargetArea(val xRange: IntRange, val yRange: IntRange)

fun launch(targetArea: TargetArea): Pair<Int, Int> {
    val xSearchRange = max(abs(targetArea.xRange.first), abs(targetArea.xRange.last))
    val ySearchRange = max(abs(targetArea.yRange.first), abs(targetArea.yRange.last))

    var highest = 0
    var unique = 0
    for (yVelocity0 in (-ySearchRange..ySearchRange + 1)) {
        for (xVelocity0 in (-xSearchRange..xSearchRange + 1)) {
            var position = Pair(0, 0)
            var velocity = Pair(xVelocity0, yVelocity0)
            var yMax = 0
            while (position.first <= targetArea.xRange.last && position.second >= targetArea.yRange.first) {
                position = Pair(position.first + velocity.first, position.second + velocity.second)
                velocity = Pair(velocity.first - if (velocity.first > 0) 1 else 0, velocity.second - 1)
                yMax = max(yMax, position.second)
                if (position.first in targetArea.xRange && position.second in targetArea.yRange) {
                    highest = max(highest, yMax)
                    unique++
                    break
                }
            }
        }
    }
    return Pair(highest, unique)
}