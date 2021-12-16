package day16

import java.io.File

fun main() {
    val transmission = File("src/day16/input.txt").readText()
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")

    println("Day Sixteen")
    println("Part One: ${parse(transmission).version()}")
    println("Part Two: ${parse(transmission).decode()}")
    println()
}

sealed class Packet(val binary: String) {
    val type = binary.drop(3).take(3).toInt(2)
    open fun version(): Int = binary.take(3).toInt(2)
    abstract fun decode(): Long
}

class Literal(binary: String) : Packet(binary) {
    override fun decode() = binary.drop(6).chunked(5).joinToString("") { it.drop(1) }.toLong(2)
}

class Operator(binary: String, private val subs: List<Packet>) : Packet(binary) {
    override fun version() = super.version() + subs.sumOf { it.version() }
    override fun decode(): Long = when (type) {
        0 -> subs.sumOf { it.decode() }
        1 -> subs.fold(1) { acc, packet -> acc * packet.decode() }
        2 -> subs.minOf { it.decode() }
        3 -> subs.maxOf { it.decode() }
        5 -> if (subs[0].decode() > subs[1].decode()) 1L else 0L
        6 -> if (subs[0].decode() < subs[1].decode()) 1L else 0L
        7 -> if (subs[0].decode() == subs[1].decode()) 1L else 0L
        else -> error("No such Type ID: $type")
    }
}

private fun parse(packet: String): Packet {
    val type = packet.drop(3).take(3).toInt(2)
    val tail = packet.drop(6)

    return when (type) {
        4 -> tail.chunked(5)
            .let { it.takeWhile { g -> g.first() == '1' } + it.first { g -> g.first() == '0' } }
            .let { Literal("${packet.take(6)}${it.joinToString("")}") }
        else -> when (tail.first()) {
            '0' -> {
                val totalLength = tail.drop(1).take(15).toInt(2)
                val subPackets: List<Packet> = buildList {
                    while (totalLength - sumOf { it.binary.length } > 0) {
                        parse(tail.drop(16 + sumOf { it.binary.length })).also { add(it) }
                    }
                }
                Operator("${packet.take(22)}${subPackets.joinToString("") { it.binary }}", subPackets)
            }
            '1' -> {
                val noSubPackets = tail.drop(1).take(11).toInt(2)
                val subPackets: List<Packet> = buildList {
                    repeat(noSubPackets) { parse(tail.drop(12 + sumOf { it.binary.length })).also { add(it) } }
                }
                Operator("${packet.take(18)}${subPackets.joinToString("") { it.binary }}", subPackets)
            }
            else -> error("No such Length Type ID: ${tail.first()}")
        }
    }
}