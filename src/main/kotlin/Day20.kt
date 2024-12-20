import kotlin.math.abs
import utils.AoCDay
import utils.Vec2D
import utils.toPointMap

class Day20 : AoCDay() {
    val maze = input.toPointMap()
    val start = maze.filterValues { it == 'S' }.keys.first()
    val end = maze.filterValues { it == 'E' }.keys.first()
    val times = mutableMapOf<Int, Int>()
    val maxX = maze.keys.maxOf { it.x }
    val maxY = maze.keys.maxOf { it.y }
    val costsFromStart = calculateCosts(start)
    val costsFromEnd = calculateCosts(end)
    val costToEnd = costsFromStart[end]!!

    data class Cheat(val start: Vec2D, val end: Vec2D, val cost: Int)

    override fun part1(): Any {
        return calculateGains(2)
    }

    override fun part2(): Any {
        return calculateGains(20)
    }

    fun calculateGains(cheatTime: Int): Int {
        val gains = mutableMapOf<Int, Int>()
        for (cheat in generateCheats(cheatTime)) {
            val cost = cheat.cost + costsFromStart[cheat.start]!! + costsFromEnd[cheat.end]!!
            val gain = costToEnd - cost
            if (gain > 0) {
                gains[gain] = gains.getOrDefault(gain, 0) + 1
            }
        }

        return gains.filterKeys { it >= 100 }.values.sum()
    }

    // bfs to calculate all costs from a given start point to every other point
    fun calculateCosts(start: Vec2D): Map<Vec2D, Int> {
        val costs = mutableMapOf<Vec2D, Int>()
        val queue = mutableListOf(start)
        costs[start] = 0
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val cost = costs.getOrDefault(current, Int.MAX_VALUE)
            for (neighbour in current.getNeighbours(true)) {
                if (neighbour in maze && maze[neighbour] != '#' && costs.getOrDefault(
                        neighbour, Int.MAX_VALUE
                    ) == Int.MAX_VALUE
                ) {
                    costs[neighbour] = cost + 1
                    queue += neighbour
                }
            }
        }
        return costs
    }

    fun generateCheats(maxCost: Int): Set<Cheat> {
        val cheats = mutableSetOf<Cheat>()
        for (x in 0..maxX) {
            for (y in 0..maxY) {
                val current = Vec2D(x, y)
                if (maze[current] == '#') continue
                for (dx in -maxCost..maxCost) {
                    val xRange = maxCost - abs(dx)
                    for (dy in -xRange..xRange) {
                        val cost = abs(dx) + abs(dy)
                        val cheatPos = current + Vec2D(dx, dy)
                        if (cost in 2..maxCost && cheatPos in maze && maze[cheatPos] != '#') {
                            cheats += Cheat(cheatPos, current, cost)
                        }
                    }
                }
            }
        }
        return cheats
    }
}

fun main() {
    Day20().execute()
}
