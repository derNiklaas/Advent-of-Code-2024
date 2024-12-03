import utils.AoCDay
import utils.splitAndMapToInt

class Day03 : AoCDay() {

    private val mulRegEx = """mul\(\d*,\d*\)""".toRegex()
    private val inactiveRegex = """don't\(\).*?do\(\)""".toRegex()
    private val code = input.joinToString("")
    override fun part1(): Any {
        val matches = mulRegEx.findAll(code)
        var result = 0L
        matches.forEach { match ->
            val numbers = match.value.drop(4).dropLast(1).splitAndMapToInt(",")
            result += numbers[0] * numbers[1]
        }
        return result
    }

    override fun part2(): Any {
        var activeCode = code.replace(inactiveRegex, "")
        val matches = mulRegEx.findAll(activeCode)
        var result = 0L
        matches.forEach { match ->
            val numbers = match.value.drop(4).dropLast(1).splitAndMapToInt(",")
            result += numbers[0] * numbers[1]
        }
        return result
    }
}

fun main() {
    Day03().execute()
}
