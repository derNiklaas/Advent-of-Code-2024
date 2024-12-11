import utils.AoCDay
import utils.isEven
import utils.splitAndMapToLong

class Day11 : AoCDay() {
    val stones = input.first().splitAndMapToLong()

    override fun part1(): Any {
        return blink(25)
    }

    override fun part2(): Any {
        return blink(75)
    }

    private fun blink(times: Int): Long {
        var map = mutableMapOf<Long, Long>()
        stones.forEach {
            map[it] = (map[it] ?: 0) + 1
        }
        repeat(times) {
            var newMap = mutableMapOf<Long, Long>()
            for ((number, amount) in map) {
                if (number == 0L) {
                    newMap[1] = (newMap[1] ?: 0) + amount
                } else if (number.toString().length.isEven()) {
                    val string = number.toString()
                    val half = string.length / 2
                    val firstHalf = string.substring(0, half).toLong()
                    val secondHalf = string.substring(half).toLong()
                    newMap[firstHalf] = (newMap[firstHalf] ?: 0) + amount
                    newMap[secondHalf] = (newMap[secondHalf] ?: 0) + amount
                } else {
                    newMap[number * 2024] = (newMap[number * 2024] ?: 0) + amount
                }
            }
            map = newMap
        }
        return map.values.sum()
    }
}

fun main() {
    Day11().execute()
}
