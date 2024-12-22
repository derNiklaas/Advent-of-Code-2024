import utils.AoCDay
import utils.cartesianSquare
import utils.mapToLong

class Day22 : AoCDay() {
    val numbers = input.mapToLong()
    override fun part1(): Any {
        return numbers.sumOf {
            var result = it
            repeat(2000) {
                result = calculateNumber(result)
            }
            result
        }
    }

    override fun part2(): Any {
        val priceChanges = numbers.map {
            val prices = mutableListOf(it % 10)
            var result = it
            repeat(2000) {
                result = calculateNumber(result)
                prices += result % 10
            }

            prices.windowed(2).map { (a, b) -> b - a to b }.windowed(4).map { l ->
                l.map { it.first } to l.last().second
            }.reversed().associate { it }
        }
        val allSequences = (-9L..9L).cartesianSquare().cartesianSquare().map { (a, b) ->
            listOf(a.first, a.second, b.first, b.second)
        }
        return allSequences.maxOf { sequence -> priceChanges.sumOf { cost -> cost[sequence] ?: 0 } }
    }

    fun calculateNumber(secretNumber: Long): Long {
        var result = secretNumber
        result = prune(mix(result * 64, result))
        result = prune(mix(result / 32, result))
        return prune(mix(result * 2048, result))
    }

    fun mix(value: Long, secretNumber: Long): Long {
        return value xor secretNumber
    }

    fun prune(secretNumber: Long): Long {
        return secretNumber % 16777216L
    }
}

fun main() {
    Day22().execute()
}