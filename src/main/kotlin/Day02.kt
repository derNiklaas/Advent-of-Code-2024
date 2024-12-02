import kotlin.math.abs
import utils.AoCDay
import utils.splitAndMapToInt

class Day02 : AoCDay() {

    val parsedInput = input.map { it.splitAndMapToInt() }

    override fun part1(): Any {
        return parsedInput.count(::isSave)
    }

    override fun part2(): Any {
        var safe = parsedInput.count(::isSave)

        var potentialSafe = parsedInput.filter { !isSave(it) }

        potentialSafe.forEach { levels ->
            for (i in 0 until levels.size) {
                // Create a copy of the list and remove the element at index i
                val newLevels = levels.toMutableList()
                newLevels.removeAt(i)
                // If the new list is safe, we have found the element to remove
                if (isSave(newLevels)) {
                    safe++
                    break
                }
            }
        }

        return safe
    }

    private fun isSave(levels: List<Int>): Boolean {
        for (i in 0 until levels.lastIndex) {
            var diff = abs(levels[i + 1] - levels[i])
            if (diff !in 1..3) return false
        }

        var sorted = levels.sorted()
        if (levels == sorted) return true
        if (levels == sorted.reversed()) return true

        return false
    }
}

fun main() {
    Day02().execute()
}
