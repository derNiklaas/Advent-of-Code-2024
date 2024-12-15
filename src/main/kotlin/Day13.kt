import kotlin.math.abs
import kotlin.math.round
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.operations.first
import org.jetbrains.kotlinx.multik.ndarray.operations.last
import utils.AoCDay
import utils.chunkedInput
import utils.mapInner
import utils.mapToLong

class Day13 : AoCDay() {
    val splits = input.chunkedInput()
        .mapInner { it.split(": ")[1].split(", ").map { it.drop(2) }.mapToLong() }
    val validDistance = 0.01

    override fun part1(): Any {
        return splits.sumOf { split ->
            val buttonA = split[0]
            val buttonB = split[1]
            val prize = split[2]
            calculateCost(buttonA, buttonB, prize)
        }
    }

    override fun part2(): Any {
        return splits.sumOf { split ->
            val buttonA = split[0]
            val buttonB = split[1]
            val prize = split[2].map { it + 10000000000000 }
            calculateCost(buttonA, buttonB, prize)
        }
    }

    fun calculateCost(buttonA: List<Long>, buttonB: List<Long>, prize: List<Long>): Long {
        val buttons = mk.ndarray(listOf(listOf(buttonA[0], buttonB[0]), listOf(buttonA[1], buttonB[1])))
        val result = mk.linalg.solve(buttons, mk.ndarray(prize))
        // println(result)
        if (abs(result.first() - round(result.first())) > validDistance || abs(result.last() - round(result.last())) > validDistance) {
            // println("Invalid result: $result")
            return 0
        }
        return round(result.first()).toLong() * 3 + round(result.last()).toLong()
    }
}

fun main() {
    Day13().execute()
}
