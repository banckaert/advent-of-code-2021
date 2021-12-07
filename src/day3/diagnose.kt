package day3

import day3.DiagnosticType.*
import java.io.File

fun main() {
    val diagnostics = File("src/day3/input.txt").readLines()

    println("Day Three")
    println("Part One: ${partOne(diagnostics)}")
    println("Part Two: ${partTwo(diagnostics)}")
    println()
}

enum class DiagnosticType {
    GAMMA, EPSILON, O2, CO2
}

private fun transform(diagnostics: List<String>): List<String> {
    val charArray = diagnostics.map(String::toCharArray)
    val transformedDiagnostics = mutableListOf<String>()
    for (column in 0 until charArray[0].size) {
        var transformed = ""
        charArray.forEach { transformed += it[column] }
        transformedDiagnostics.add(transformed)
    }
    return transformedDiagnostics
}

private fun moreZeros(transformed: List<String>) = transformed.map { bit -> bit.count { it == '0' } > bit.length / 2 }
private fun binaryToInt(binary: String) = binary.toInt(2)

private fun partOne(diagnostics: List<String>): Int {
    var gamma = ""
    var epsilon = ""
    moreZeros(transform(diagnostics)).forEach {
        gamma += decode(GAMMA, it)
        epsilon += decode(EPSILON, it)
    }
    return binaryToInt(gamma) * binaryToInt(epsilon)
}

private fun step(type: DiagnosticType, remaining: List<String>, step: Int) =
    decode(type, moreZeros(transform(remaining))[step])

private fun decode(type: DiagnosticType, moreZeros: Boolean) = when (type) {
    GAMMA, O2 -> if (moreZeros) '0' else '1'
    EPSILON, CO2 -> if (moreZeros) '1' else '0'
}

private fun remaining(diagnostics: List<String>, prefix: String) = diagnostics.filter { o -> o.startsWith(prefix) }

private fun diagnose(diagnostics: List<String>, type: DiagnosticType): Int {
    var prefix = ""
    var remainingDiagnostics = diagnostics
    for (step in remainingDiagnostics.indices) {
        prefix += step(type, remainingDiagnostics, step)
        remainingDiagnostics = remaining(diagnostics, prefix)
        if (remainingDiagnostics.size == 1) return binaryToInt(remainingDiagnostics[0])
    }
    return 0
}

private fun partTwo(diagnostics: List<String>) = diagnose(diagnostics, O2) * diagnose(diagnostics, CO2)