import kotlin.math.max
import utils.AoCDay
import utils.Vec2D

class Day08 : AoCDay() {
    val map = mutableMapOf<Char, List<Vec2D>>()

    init {
        for (y in input.indices) {
            for (x in input[0].indices) {
                val char = input[y][x]
                if (char == '.') continue
                if (char !in map) map[char] = mutableListOf()
                map[char] = map[char]!! + Vec2D(x, y)
            }
        }
    }

    override fun part1(): Any {
        return solve()
    }

    override fun part2(): Any {
        return solve(true)
    }

    private fun solve(partB: Boolean = false): Int {
        val antiNodes = mutableSetOf<Vec2D>()
        val maxSize = if (partB) max(input.size, input[0].length) else 1

        for ((_, positions) in map) {
            for (posA in positions) {
                for (posB in positions) {
                    if (posA == posB) continue

                    repeat(maxSize + 1) { i ->
                        if (!partB && i == 0) return@repeat
                        antiNodes += posA + (posA - posB) * i
                        antiNodes += posB + (posB - posA) * i
                    }

                }
            }
        }
        return antiNodes.filter { it.y in input.indices && it.x in input[0].indices }.size
    }
}

fun main() {
    Day08().execute()
}
