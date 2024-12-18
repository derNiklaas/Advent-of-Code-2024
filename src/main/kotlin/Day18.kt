import java.util.PriorityQueue
import utils.AoCDay
import utils.Vec2D
import utils.splitAndMapToInt

class Day18 : AoCDay() {
    val positions = input.map {
        val (x, y) = it.splitAndMapToInt(",")
        Vec2D(x, y)
    }

    val dim = 70
    val start = Vec2D(0, 0)
    val end = Vec2D(dim, dim)

    override fun part1(): Any {
        val fallen = mutableSetOf<Vec2D>()
        positions.take(1024).forEach { fallen += it }

        fun search(): Int {
            val queue = PriorityQueue<Pair<Vec2D, Int>>(compareBy { it.second })
            queue += start to 0
            val visited = mutableSetOf<Vec2D>()
            while (queue.isNotEmpty()) {
                val (current, steps) = queue.poll()
                if (current in visited) continue
                if (current == end) return steps
                visited += current
                for (neighbour in current.getNeighbours(true)) {
                    if (neighbour !in fallen && neighbour.x in 0..dim && neighbour.y in 0..dim) {
                        queue += neighbour to steps + 1
                    }
                }
            }
            error("no path")
        }

        return search()
    }

    override fun part2(): Any {
        fun check(n: Int): Boolean {
            val blocks = positions.take(n).toSet()
            val queue = PriorityQueue<Pair<Vec2D, Int>>(compareBy { it.second })
            queue += start to 0
            val visited = mutableSetOf<Vec2D>()
            while (queue.isNotEmpty()) {
                val (current, steps) = queue.poll()
                if (current in visited) continue
                if (current == end) return true
                visited += current
                for (neighbour in current.getNeighbours(true)) {
                    if (neighbour !in blocks && neighbour.x in 0..dim && neighbour.y in 0..dim) {
                        queue += neighbour to steps + 1
                    }
                }
            }
            return false
        }

        tailrec fun binarySearch(low: Int, high: Int): Int {
            val n = (low + high) / 2
            return if (n == low) n
            else if (check(n)) binarySearch(n, high)
            else binarySearch(low, n)
        }

        val limit = binarySearch(1024, positions.size)
        val pos = positions.drop(limit).first()
        return "${pos.x},${pos.y}"
    }
}

fun main() {
    Day18().execute()
}
