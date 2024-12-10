import java.util.LinkedList
import utils.AoCDay
import utils.Vec2D
import utils.get
import utils.getOrNull

class Day10 : AoCDay() {

    val map = input.map { it.map { it.digitToIntOrNull() ?: -1 }.toList() }
    val starts = mutableListOf<Vec2D>()

    init {
        for (i in map.indices) {
            for (j in map[0].indices) {
                if (map[i][j] == 0) {
                    starts += Vec2D(j, i)
                }
            }
        }
    }

    override fun part1(): Any {
        return starts.sumOf { bfs(it) }
    }

    override fun part2(): Any {
        return starts.sumOf { bfs(it, true) }
    }

    fun bfs(start: Vec2D, ignoreFilter: Boolean = false): Long {
        var count = 0L
        val visited = mutableSetOf<Vec2D>()
        val queue = LinkedList<Vec2D>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.y !in map.indices || current.x !in map[0].indices) continue
            if (map[current] == 9) {
                count++
            }

            current.getNeighbours(true).forEach {
                if (ignoreFilter || it !in visited) {
                    val nextValue = (map.getOrNull(it) ?: -1)
                    val check = nextValue - map[current] == 1
                    if (check) {
                        queue.add(it)
                        visited.add(it)
                    }
                }
            }
        }
        return count
    }
}

fun main() {
    Day10().execute()
}
