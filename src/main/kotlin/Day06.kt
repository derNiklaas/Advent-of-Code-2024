import utils.AoCDay
import utils.Vec2D

class Day06 : AoCDay() {
    val map = input.map { it.toList() }
    val obstacles = mutableSetOf<Vec2D>()
    val maxY = map.size
    val maxX = map[0].size
    lateinit var start: Vec2D
    var firstPart = 0
    var secondPart = 0

    init {
        for (i in map.indices) {
            for (j in map[0].indices) {
                if (map[i][j] == '^') {
                    start = Vec2D(j, i)
                } else if (map[i][j] == '#') {
                    obstacles.add(Vec2D(j, i))
                }
            }
        }
        var (_, a, b) = checkLoop(obstacles)
        firstPart = a
        secondPart = b
    }

    override fun part1(): Any {
        return firstPart
    }

    override fun part2(): Any {
        return secondPart
    }

    private fun turn(direction: Vec2D): Vec2D {
        return when (direction) {
            Vec2D.UP -> Vec2D.RIGHT
            Vec2D.RIGHT -> Vec2D.DOWN
            Vec2D.DOWN -> Vec2D.LEFT
            Vec2D.LEFT -> Vec2D.UP
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }

    private fun checkLoop(
        obstacles: Set<Vec2D>,
        checkLoop: Boolean = false
    ): Triple<Boolean, Int, Int> {
        var counter = 0
        val visited = mutableSetOf<Pair<Vec2D, Vec2D>>(start to Vec2D.UP)
        var currentLocation = start
        var direction = Vec2D.UP
        while (true) {
            val nextPos = currentLocation + direction
            if (nextPos.x !in 0 until maxX || nextPos.y !in 0 until maxY) {
                break
            }

            if (nextPos in obstacles) {
                direction = turn(direction)
            } else {
                currentLocation = nextPos
                var directionPair = currentLocation to direction

                if (directionPair in visited) {
                    return Triple(true, 0, 0)
                }
                visited += directionPair
            }
        }

        var uniqueLocations = visited.distinctBy { it.first }

        if (!checkLoop) {
            for (pos in uniqueLocations) {
                if (pos.first == start) continue
                val newObstacles = obstacles.toMutableSet() + pos.first
                if (checkLoop(newObstacles, true).first) {
                    counter++
                }
            }
        }

        return Triple(false, uniqueLocations.size, counter)
    }

    fun printMap(visited: Set<Pair<Vec2D, Vec2D>>, obstacles: Set<Vec2D>) {
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                val pos = Vec2D(x, y)
                var char = '.'
                if (pos in obstacles) {
                    char = '#'
                } else {
                    val movements = visited.filter { it.first == pos }
                    val topDown = movements.filter { it.second == Vec2D.UP || it.second == Vec2D.DOWN }
                    val leftRight = movements.filter { it.second == Vec2D.LEFT || it.second == Vec2D.RIGHT }
                    if (topDown.size != 0 && leftRight.size != 0) {
                        char = '+'
                    } else if (topDown.size != 0) {
                        char = '|'
                    } else if (leftRight.size != 0) {
                        char = '-'
                    }
                }
                print(char)
            }
            println()
        }
    }
}

fun main() {
    Day06().execute()
}
