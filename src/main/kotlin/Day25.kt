import utils.AoCDay
import utils.Vec2D
import utils.chunkedInput
import utils.toPointMap

class Day25 : AoCDay() {

    val chunks = input.chunkedInput().map { it.toPointMap() }
    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()

    init {
        chunks.forEach { chunk ->
            val isKey = chunk[Vec2D(0, 0)] == '.'
            if (isKey) {
                keys += chunk.filter { it.value == '#' }.keys.sortedBy { it.y }.distinctBy { it.x }.sortedBy { it.x }
                    .map { 6 - it.y }
            } else {
                locks += chunk.filter { it.value == '#' }.keys.sortedByDescending { it.y }.distinctBy { it.x }
                    .sortedBy { it.x }.map { it.y }
            }
        }
    }

    override fun part1(): Any {
        return keys.sumOf { key ->
            locks.count { lock ->
                for (i in 0..4) {
                    if (key[i] + lock[i] > 5) {
                        return@count false
                    }
                }
                true
            }
        }
    }

    override fun part2(): Any {
        return "Merry Christmas!"
    }
}

fun main() {
    Day25().execute()
}
