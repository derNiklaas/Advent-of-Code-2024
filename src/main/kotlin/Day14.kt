import kotlin.system.exitProcess
import utils.AoCDay
import utils.Vec2D
import utils.splitAndMapToInt

class Day14 : AoCDay() {
    val robots = input.map {
        val (loc, dir) = it.split(" ").map {
            it.drop(2).splitAndMapToInt(",")
        }
        Vec2D(loc.first(), loc.last()) to Vec2D(dir.first(), dir.last())
    }
    val spaceWidth = 101
    val spaceHeight = 103

    override fun part1(): Any {
        val locations = simulate(100)
        val middleX = spaceWidth / 2
        val middleY = spaceHeight / 2
        val TL = locations.filter { it.x in 0..<middleX && it.y in 0..<middleY }.size
        val TR = locations.filter { it.x in (middleX + 1)..spaceWidth && it.y in 0..<middleY }.size
        val BL = locations.filter { it.x in 0..<middleX && it.y in (middleY + 1)..spaceHeight }.size
        val BR = locations.filter { it.x in (middleX + 1)..spaceWidth && it.y in (middleY + 1)..spaceHeight }.size
        //println("$TL $TR $BL $BR")
        return TL.toLong() * TR * BL * BR
    }

    override fun part2(): Any {
        simulate(Int.MAX_VALUE)
        error("This shouldn't happen")
    }

    fun simulate(times: Int): List<Vec2D> {
        var space = robots.toMutableList()
        repeat(times) {
            val newSpace = mutableListOf<Pair<Vec2D, Vec2D>>()
            for ((loc, dir) in space) {
                var newLoc = loc + dir
                while (newLoc.y < 0) {
                    newLoc += Vec2D(0, spaceHeight)
                }
                while (newLoc.y >= spaceHeight) {
                    newLoc -= Vec2D(0, spaceHeight)
                }
                while (newLoc.x < 0) {
                    newLoc += Vec2D(spaceWidth, 0)
                }
                while (newLoc.x >= spaceWidth) {
                    newLoc -= Vec2D(spaceWidth, 0)
                }
                //println("$loc $dir -> $newLoc")
                newSpace += newLoc to dir
            }
            space = newSpace
            printSpace(space.map { it.first }, true, it + 1)
        }
        return space.map { it.first }
    }

    fun printSpace(robots: List<Vec2D>, lookForTree: Boolean, iteration: Int) {
        var potentialTree = false
        for (y in 0 until spaceHeight) {
            var row = ""
            for (x in 0 until spaceWidth) {
                val count = robots.count { it == Vec2D(x, y) }
                row += if (count == 0) {
                    "."
                } else {
                    "$count"
                }
            }
            if (row.contains("111111111") && lookForTree) {
                potentialTree = true
            }
            if (!lookForTree) {
                println(row)
            }
        }
        if (lookForTree && potentialTree) {
            printSpace(robots, false, iteration)
            println()
            println("Is this a tree? (y/n)")
            val input = readln()
            if (input == "y") {
                println("Part B: $iteration")
                exitProcess(0)
            }
        }
    }
}

fun main() {
    Day14().execute()
}
