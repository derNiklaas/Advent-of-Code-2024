import kotlin.math.abs
import utils.AoCDay
import utils.splitAndMapToInt

class Day01 : AoCDay() {

    var rightList = listOf<Int>()
    var leftList = listOf<Int>()

    init {
        input.forEach { line ->
            val split = line.splitAndMapToInt(" ")
            leftList += split[0]
            rightList += split[1]
        }
        rightList = rightList.sorted()
        leftList = leftList.sorted()
    }

    override fun part1(): Any {
        var results = 0
        for (i in rightList.indices) {
            val left = leftList[i]
            val right = rightList[i]
            results += abs(right - left)
        }
        return results
    }

    override fun part2(): Any {
        var results = 0L
        for (i in rightList.indices) {
            val left = leftList[i]
            var count = rightList.count { it == left }
            results += left * count
        }
        return results
    }
}

fun main() {
    Day01().execute()
}
