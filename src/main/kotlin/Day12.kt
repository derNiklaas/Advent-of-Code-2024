import utils.AoCDay
import utils.Vec2D
import utils.toPointMap

class Day12 : AoCDay() {

    val map = input.toPointMap()
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }
    val result = calculateFenceCost()

    override fun part1(): Any {
        return result.first
    }

    override fun part2(): Any {
        return result.second
    }

    fun calculateFenceCost(): Pair<Long, Long> {
        val visited = mutableSetOf<Vec2D>()
        var count = 0L
        var countB = 0L
        for (x in 0..maxX) {
            for (y in 0..maxY) {
                val point = Vec2D(x, y)
                if (point in visited) continue
                val currentIdentifier = map[point]!!
                val queue = mutableListOf(point)
                val region = mutableSetOf<Vec2D>()
                val edges = mutableListOf<Pair<Vec2D, Vec2D>>()
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    if (current in region) continue
                    region.add(current)
                    for (neighbour in current.getNeighbours(true)) {
                        if (map[neighbour] != currentIdentifier) {
                            edges.add(current to (neighbour - current))
                            continue
                        }
                        queue.add(neighbour)
                    }
                }
                count += region.size * edges.size
                var amountOfEdges = 0
                while (edges.isNotEmpty()) {
                    val (current, direction) = edges.removeFirst()
                    amountOfEdges++
                    // check positive side of direction
                    var i = 1
                    var offset = Vec2D(direction.y, direction.x)
                    while (edges.contains((current + offset * i) to direction)) {
                        edges.remove((current + offset * i) to direction)
                        i++
                    }
                    // check negative side of direction
                    i = -1
                    while (edges.contains((current + offset * i) to direction)) {
                        edges.remove((current + offset * i) to direction)
                        i--
                    }
                }
                countB += region.size * amountOfEdges
                visited.addAll(region)
            }
        }
        return count to countB
    }
}

fun main() {
    Day12().execute()
}
