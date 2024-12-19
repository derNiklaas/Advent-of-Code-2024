import utils.AoCDay
import utils.chunkedInput

class Day19 : AoCDay() {
    val chunks = input.chunkedInput()
    val patterns = chunks[0].first().split(", ")
    val towels = chunks[1]
    val matchCounts = towels.map { countMatches(it) }

    override fun part1(): Any {
        return towels.indices.count { matchCounts[it] > 0 }
    }

    override fun part2(): Any {
        return matchCounts.sum()
    }

    fun countMatches(design: String): Long {
        val cache = LongArray(design.length + 1) { 0L }
        cache[0] = 1L
        for (index in design.indices) {
            val currentCount = cache[index]
            for (pattern in patterns) {
                if (design.substring(index).startsWith(pattern)) {
                    cache[index + pattern.length] += currentCount
                }
            }
        }
        return cache[design.length]
    }
}

fun main() {
    Day19().execute()
}
