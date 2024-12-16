import utils.AoCDay
import utils.Vec2D
import utils.toPointMap

class Day16 : AoCDay() {
    val map = input.toPointMap()
    val start = map.entries.first { it.value == 'S' }.key
    val end = map.entries.first { it.value == 'E' }.key

    private data class State(val pos: Vec2D, val dir: Vec2D)
    private data class StateWithPath(val state: State, val path: Set<Vec2D>)

    override fun part1(): Any {
        return costOfShortestPath(State(start, Vec2D.RIGHT))
    }

    override fun part2(): Any {
        return shortestPaths(State(start, Vec2D.RIGHT))
    }

    private fun costOfShortestPath(state: State): Int {
        val queue = sortedMapOf(0 to listOf(state))
        val visited = mutableSetOf(state)
        while (queue.isNotEmpty()) {
            val currentCost = queue.firstKey()
            val neighbours = queue.remove(currentCost)!!

            if (neighbours.any { it.pos == end }) {
                return currentCost
            }

            for (neighbour in neighbours) {
                if (map[neighbour.pos] == '#') continue
                val pos = neighbour.pos
                val dir = neighbour.dir
                val nextStates = mapOf(
                    State(pos + dir, dir) to 1,
                    State(pos, dir.turnClockwise()) to 1000,
                    State(pos, dir.turnAntiClockwise()) to 1000
                )
                nextStates
                    .filterKeys { map[it.pos] != '#' }
                    .filterKeys { it !in visited }
                    .mapValues { it.value + currentCost }.forEach {
                        visited += it.key
                        queue.merge(it.value, listOf(it.key)) { a, b -> a + b }
                    }
            }
        }
        return -1
    }

    private fun shortestPaths(state: State): Int {
        val queue = sortedMapOf(0 to listOf(StateWithPath(state, setOf(state.pos))))
        val visited = mutableMapOf(state to 0)

        while (queue.isNotEmpty()) {
            val currentCost = queue.firstKey()
            val neighbours = queue.remove(currentCost)!!

            if (neighbours.any { it.state.pos == end }) {
                return neighbours.filter { it.state.pos == end }.map { it.path }.reduce { acc, set -> acc + set }.size
            }

            for (neighbour in neighbours) {
                if (map[neighbour.state.pos] == '#') continue
                val pos = neighbour.state.pos
                val dir = neighbour.state.dir
                val nextStates = mapOf(
                    State(pos + dir, dir) to 1,
                    State(pos, dir.turnClockwise()) to 1000,
                    State(pos, dir.turnAntiClockwise()) to 1000
                )
                nextStates
                    .filterKeys { map[it.pos] != '#' }
                    .mapValues { it.value + currentCost }
                    .filter { visited.getOrDefault(it.key, Int.MAX_VALUE) >= it.value }
                    .forEach {
                        visited[it.key] = it.value
                        queue.merge(
                            it.value, listOf(StateWithPath(it.key, neighbour.path + it.key.pos))
                        ) { a, b -> a + b }
                    }
            }
        }
        return -1
    }

    private fun Vec2D.turnClockwise(): Vec2D {
        return when (this) {
            Vec2D.UP -> Vec2D.RIGHT
            Vec2D.RIGHT -> Vec2D.DOWN
            Vec2D.DOWN -> Vec2D.LEFT
            Vec2D.LEFT -> Vec2D.UP
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }

    private fun Vec2D.turnAntiClockwise(): Vec2D {
        return when (this) {
            Vec2D.UP -> Vec2D.LEFT
            Vec2D.RIGHT -> Vec2D.UP
            Vec2D.DOWN -> Vec2D.RIGHT
            Vec2D.LEFT -> Vec2D.DOWN
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }
}

fun main() {
    Day16().execute()
}
