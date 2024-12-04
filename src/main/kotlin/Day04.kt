import utils.AoCDay
import utils.Vec2D

class Day04 : AoCDay() {
    val grid = input.map { it.toCharArray() }
    override fun part1(): Any {
        var count = 0L
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val pos = Vec2D(i, j)
                if (grid[i][j] != 'X') continue
                val directions = listOf(
                    Vec2D.UP,
                    Vec2D.UP + Vec2D.RIGHT,
                    Vec2D.RIGHT,
                    Vec2D.RIGHT + Vec2D.DOWN,
                    Vec2D.DOWN,
                    Vec2D.DOWN + Vec2D.LEFT,
                    Vec2D.LEFT,
                    Vec2D.LEFT + Vec2D.UP
                )
                for (direction in directions) {
                    var string = ""
                    for (k in 0..3) {
                        val newPos = pos + direction * k
                        if (newPos.x !in grid.indices || newPos.y !in grid[i].indices) {
                            break
                        }
                        string += grid[newPos.x][newPos.y]
                    }
                    if (string.length != 4) continue
                    if (string == "XMAS") {
                        count++
                    }
                }
            }
        }

        return count
    }

    override fun part2(): Any {
        var count = 0L
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] != 'A') continue
                if (i == 0 || i == grid.size - 1 || j == 0 || j == grid[i].size - 1) continue
                if (grid[i - 1][j - 1] == 'M' && grid[i + 1][j - 1] == 'M' && grid[i - 1][j + 1] == 'S' && grid[i + 1][j + 1] == 'S') count++
                if (grid[i - 1][j - 1] == 'M' && grid[i + 1][j - 1] == 'S' && grid[i - 1][j + 1] == 'M' && grid[i + 1][j + 1] == 'S') count++
                if (grid[i - 1][j - 1] == 'S' && grid[i + 1][j - 1] == 'S' && grid[i - 1][j + 1] == 'M' && grid[i + 1][j + 1] == 'M') count++
                if (grid[i - 1][j - 1] == 'S' && grid[i + 1][j - 1] == 'M' && grid[i - 1][j + 1] == 'S' && grid[i + 1][j + 1] == 'M') count++
            }
        }

        return count
    }
}

fun main() {
    Day04().execute()
}
