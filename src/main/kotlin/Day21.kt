import utils.AoCDay
import utils.Vec2D

class Day21 : AoCDay() {

    val codepad = "789456123 0A".withIndex().associate { (index, char) -> char to Vec2D(index % 3, index / 3) }
    val navpad = " ^A<v>".withIndex().associate { (index, char) -> char to Vec2D(index % 3, index / 3) }

    override fun part1(): Any {
        return solve(2)
    }

    override fun part2(): Any {
        return solve(25)
    }

    fun solve(robots: Int): Long {
        val cache = mutableMapOf<State, Long>()
        return input.sumOf {
            val num = it.dropLast(1).toInt()
            val cost = "A$it".zipWithNext { a, b ->
                val from = codepad[a]!!
                val to = codepad[b]!!
                navigate(State(from, to, robots), codepad[' ']!!, cache)
            }.sum()
            num * cost
        }
    }

    data class State(val from: Vec2D, val to: Vec2D, val robots: Int)

    fun navigate(state: State, avoid: Vec2D, cache: MutableMap<State, Long>): Long {
        cache[state]?.let { return it }

        var min = Long.MAX_VALUE

        val queue = ArrayDeque<Pair<Vec2D, String>>()
        queue += state.from to "A"

        while (queue.isNotEmpty()) {
            val (at, pressed) = queue.removeFirst()
            if (at == state.to) {
                val cost = when (state.robots) {
                    0 -> pressed.length.toLong()
                    else -> {
                        "${pressed}A".zipWithNext { na, nb ->
                            val a = navpad[na]!!
                            val b = navpad[nb]!!
                            navigate(State(a, b, state.robots - 1), navpad[' ']!!, cache)
                        }.sum()
                    }
                }
                min = minOf(min, cost)
            } else if (at != avoid) {
                if (at.x > state.to.x) queue += Vec2D(at.x - 1, at.y) to "${pressed}<"
                if (at.x < state.to.x) queue += Vec2D(at.x + 1, at.y) to "${pressed}>"
                if (at.y > state.to.y) queue += Vec2D(at.x, at.y - 1) to "${pressed}^"
                if (at.y < state.to.y) queue += Vec2D(at.x, at.y + 1) to "${pressed}v"
            }
        }

        cache[state] = min
        return min
    }
}

fun main() {
    Day21().execute()
}
