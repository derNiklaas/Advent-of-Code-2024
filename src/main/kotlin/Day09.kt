import utils.AoCDay
import utils.splitAndMapToInt

class Day09 : AoCDay() {
    val lengths = input.first().splitAndMapToInt("")
    val writes = mutableMapOf<Int, Int>()
    val spaces = mutableMapOf<Int, Int>()

    init {
        var id = 0
        lengths.forEachIndexed { index, length ->
            if (index % 2 == 0) {
                writes += id to length
                id++
            } else {
                spaces += (id) to length
            }
        }
    }

    // 2333133121414131402
    // 2 3 1 3 2 4 4 3 4 2

    override fun part1(): Any {
        var writeState = true
        var result = 0L

        var spaces = this.spaces.toMutableMap()
        var writes = this.writes.toMutableMap()

        var totalIndex = 0

        while (writes.isNotEmpty()) {
            if (writeState) {
                val (id, amount) = writes.entries.first()
                writes.remove(id, amount)
                repeat(amount) {
                    result += id * totalIndex.toLong()
                    totalIndex++
                }
                writeState = false
            } else {
                if (spaces.isEmpty()) {
                    writeState = true
                    continue
                }
                val (spaceId, amountOfSpaces) = spaces.entries.first()
                spaces.remove(spaceId, amountOfSpaces)

                repeat(amountOfSpaces) {
                    val (id, amountOfWrites) = writes.entries.last()
                    writes.remove(id, amountOfWrites)
                    if (amountOfWrites != 1) {
                        writes += id to amountOfWrites - 1
                    }
                    result += id * totalIndex
                    totalIndex++
                }
                writeState = true
            }
        }

        return result
    }

    override fun part2(): Any {
        return -1
    }
}

fun main() {
    Day09().execute()
}
