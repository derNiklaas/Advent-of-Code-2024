import utils.AoCDay

class Day23 : AoCDay() {

    val edges = mutableMapOf<String, Set<String>>()

    init {
        for (line in input) {
            val (from, to) = line.split("-")
            edges[from] = edges.getOrDefault(from, setOf()) + to
            edges[to] = edges.getOrDefault(to, setOf()) + from
        }
    }

    override fun part1(): Any {
        val connectedComputers = mutableSetOf<Set<String>>()
        for (computerA in edges.keys) {
            for (computerB in edges.keys) {
                for (computerC in edges.keys) {
                    if (computerA != computerB && computerB != computerC && computerA != computerC) {
                        if (computerB in edges[computerA]!! && computerC in edges[computerA]!! && computerC in edges[computerB]!!) {
                            connectedComputers += setOf(computerA, computerB, computerC)
                        }
                    }
                }
            }
        }
        return connectedComputers.count { it.any { it.startsWith("t") } }
    }

    override fun part2(): Any {
        val nodes = edges.keys.sorted()
        val current = Array(nodes.size) { "" }
        var best = emptyArray<String>()
        fun find(start: Int, n: Int) {
            if (n > best.size) {
                best = current.copyOfRange(0, n)
            }
            loop@ for (i in start..<nodes.size) {
                val nodeI = nodes[i]
                val edgesI = edges[nodeI]!!
                for (j in 0..<n) {
                    val nodeJ = current[j]
                    if (nodeJ !in edgesI) continue@loop
                }
                current[n] = nodeI
                find(i + 1, n + 1)
            }
        }
        find(0, 0)
        return best.joinToString(",")
    }
}

fun main() {
    Day23().execute()
}
