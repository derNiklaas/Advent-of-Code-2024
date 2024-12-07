import utils.AoCDay
import utils.splitAndMapToLong

class Day07 : AoCDay() {

    val equations = input.map {
        val parts = it.split(":")
        parts[0].toLong() to parts[1].splitAndMapToLong()
    }

    override fun part1(): Any {
        return equations.filter { solve(it.second, it.first) }.sumOf { it.first }
    }

    override fun part2(): Any {
        return equations.filter { solve(it.second, it.first, true) }.sumOf { it.first }
    }

    fun solve(numbers: List<Long>, target: Long, partB: Boolean = false): Boolean {
        if (numbers.size == 1) {
            return numbers.first() == target
        }
        if (numbers.first() > target) {
            return false
        }
        val unused = numbers.drop(2)
        val addition = mutableListOf(numbers[0] + numbers[1])
        val multiplication = mutableListOf(numbers[0] * numbers[1])
        addition.addAll(unused)
        multiplication.addAll(unused)
        val result = solve(addition, target, partB) || solve(multiplication, target, partB)

        if (!partB) {
            return result
        }

        val concat = mutableListOf("${numbers[0]}${numbers[1]}".toLong())
        return result || solve(concat + unused, target, true)
    }
}

fun main() {
    Day07().execute()
}
