import utils.AoCDay

class Day09 : AoCDay() {
    val blocks = input.first().mapIndexed { index, char ->
        if (index % 2 == 0) {
            Block.File(index / 2, char.digitToInt())
        } else {
            Block.Space(char.digitToInt())
        }
    }

    override fun part1(): Any {
        val blocks = this.blocks.toMutableList()

        while (blocks.any { it is Block.Space }) {
            var block = blocks.removeLast()
            if (block is Block.File) {
                var file: Block.File? = block
                while (file != null) {
                    val spaceIndex = blocks.indexOfFirst { it is Block.Space }
                    if (spaceIndex == -1) {
                        blocks.add(file)
                        file = null
                    } else {
                        val space = blocks.removeAt(spaceIndex)
                        if (space.size > file.size) {
                            blocks.add(spaceIndex, Block.Space(space.size - file.size))
                        }
                        if (space.size >= file.size) {
                            blocks.add(spaceIndex, file)
                            file = null
                        } else {
                            blocks.add(spaceIndex, Block.File(file.id, space.size))
                            file = Block.File(file.id, file.size - space.size)
                        }
                    }
                }
            }
        }

        return checksum(blocks)
    }

    override fun part2(): Any {
        val blocks = this.blocks.toMutableList()
        for (index in blocks.indices.reversed()) {
            val block = blocks[index]
            if (block is Block.File) {
                val spaceIndex = blocks.indexOfFirst { it is Block.Space && it.size >= block.size }
                if (spaceIndex != -1 && spaceIndex < index) {
                    blocks[index] = Block.Space(block.size)
                    val space = blocks.removeAt(spaceIndex)
                    if (space.size > block.size) {
                        blocks.add(spaceIndex, Block.Space(space.size - block.size))
                    }
                    blocks.add(spaceIndex, block)
                }
            }
        }

        return checksum(blocks)
    }

    fun checksum(blocks: List<Block>): Long {
        var result = 0L
        var i = 0
        blocks.forEach { block ->
            repeat(block.size) {
                if (block is Block.File) {
                    result += block.id * i
                }
                i++
            }
        }

        return result
    }


    sealed interface Block {
        val size: Int

        data class File(val id: Int, override val size: Int) : Block
        data class Space(override val size: Int) : Block
    }

}

fun main() {
    Day09().execute()
}
