import utils.AoCDay
import utils.Vec2D
import utils.chunkedInput
import utils.toPointMap

class Day15 : AoCDay() {

    val chunks = input.chunkedInput()
    val grid = chunks.first().toPointMap().toMutableMap()
    val instructions = chunks.last().joinToString("").map {
        when (it) {
            '^' -> Vec2D.UP
            'v' -> Vec2D.DOWN
            '>' -> Vec2D.RIGHT
            '<' -> Vec2D.LEFT
            else -> error("Invalid instruction $it")
        }
    }

    override fun part1(): Any {
        val boxes = grid.keys.filter { grid[it] == 'O' || grid[it] == '@' }.toSet()
        val walls = grid.keys.filter { grid[it] == '#' }.toSet()
        var start = grid.filter { it.value == '@' }.entries.first().key

        val (finalRobot, finalBoxes) = instructions.fold(start to boxes) { (robot, boxes), dir ->
            val toMove = generateSequence(robot) { it + dir }
                .takeWhile { it in boxes }
                .toList()
            if (toMove.last() + dir in walls) {
                robot to boxes
            } else {
                val boxesToStay = boxes - toMove.toSet()

                (robot + dir) to (boxesToStay + toMove.map { it + dir }.toSet())
            }
        }

        return (finalBoxes - finalRobot).sumOf { it.y * 100 + it.x }
    }

    override fun part2(): Any {
        val boxes = grid.keys.filter { grid[it] == 'O' }.map {
            Vec2D(it.x * 2, it.y) to Vec2D(it.x * 2 + 1, it.y)
        }.toSet()

        val walls = grid.keys.filter { grid[it] == '#' }.map {
            listOf(Vec2D(it.x * 2, it.y), Vec2D(it.x * 2 + 1, it.y))
        }.flatten().toSet()

        var start = grid.filter { it.value == '@' }.entries.first().key.let { Vec2D(it.x * 2, it.y) }

        val (_, finalBoxes) = instructions.fold(start to boxes) { (robot, boxes), dir ->
            val movingBlocks = generateSequence(setOf(robot)) { current ->
                val next = current.map { it + dir }
                val front = boxes.filter { it.first in next || it.second in next }

                current + front.flatMap { it.toList() }
            }
                .zipWithNext()
                .first { it.first == it.second }
                .first

            if (movingBlocks.any { it + dir in walls }) {
                robot to boxes
            } else {
                val (boxesToMove, boxesToStay) = boxes.partition { it.first in movingBlocks }

                (robot + dir) to (boxesToStay + boxesToMove.map { it.first + dir to it.second + dir }).toSet()

            }
        }

        return finalBoxes.sumOf { it.first.y * 100 + it.first.x }
    }
}

fun main() {
    Day15().execute()
}
