import kotlin.math.pow
import utils.AoCDay
import utils.chunkedInput
import utils.mapInner
import utils.mapToLong
import utils.splitAndMapToLong

class Day17 : AoCDay() {
    val chunks = input.chunkedInput().mapInner { it.split(": ")[1] }
    val registers = chunks[0].mapToLong().toMutableList()
    val instructions = chunks[1].splitAndMapToLong(",").flatten()

    override fun part1(): Any {
        return executeProgram().joinToString(",")
    }

    override fun part2(): Any {
        return matchOutput(instructions, instructions)
    }

    private fun matchOutput(program: List<Long>, goal: List<Long>): Long {
        val multiplier = if (goal.size == 1) 0 else 8 * matchOutput(program, goal.subList(1, goal.size))
        return multiplier.let { start ->
            generateSequence(start) { it + 1 }.first { executeProgram(it) == goal }
        }
    }

    fun executeProgram(overwriteA: Long = registers[0]): List<Long> {
        val registers = mutableListOf(overwriteA, this.registers[1], this.registers[2])
        registers[0] = overwriteA
        var instructionPointer = 0
        val output = mutableListOf<Long>()
        while (true) {
            if (instructionPointer >= instructions.size) break
            val instruction = instructions[instructionPointer]
            when (instruction) {
                0L -> {
                    val comboOperand = instructions[instructionPointer + 1]
                    val power = 2.0.pow(getComboMeaning(registers, comboOperand).toDouble()).toLong()
                    val result = registers[0] / power
                    registers[0] = result
                    instructionPointer += 2
                }

                1L -> {
                    val literalOperand = instructions[instructionPointer + 1]
                    registers[1] = registers[1] xor literalOperand
                    instructionPointer += 2
                }

                2L -> {
                    val comboOperand = instructions[instructionPointer + 1]
                    val modulo = getComboMeaning(registers, comboOperand) % 8
                    registers[1] = modulo
                    instructionPointer += 2
                }

                3L -> {
                    if (registers[0] == 0L) {
                        instructionPointer += 2
                        continue
                    }
                    val literalOperand = instructions[instructionPointer + 1]
                    instructionPointer = literalOperand.toInt()
                }

                4L -> {
                    registers[1] = registers[1] xor registers[2]
                    instructionPointer += 2
                }

                5L -> {
                    val comboOperand = instructions[instructionPointer + 1]
                    output += getComboMeaning(registers, comboOperand) % 8L
                    instructionPointer += 2
                }

                6L -> {
                    val comboOperand = instructions[instructionPointer + 1]
                    val power = 2.0.pow(getComboMeaning(registers, comboOperand).toDouble()).toLong()
                    val result = registers[0] / power
                    registers[1] = result
                    instructionPointer += 2
                }

                7L -> {
                    val comboOperand = instructions[instructionPointer + 1]
                    val power = 2.0.pow(getComboMeaning(registers, comboOperand).toDouble()).toLong()
                    val result = registers[0] / power
                    registers[2] = result
                    instructionPointer += 2
                }
            }
        }
        return output
    }

    fun getComboMeaning(registers: List<Long>, operand: Long): Long {
        return when (operand) {
            0L -> 0L
            1L -> 1L
            2L -> 2L
            3L -> 3L
            4L -> registers[0]
            5L -> registers[1]
            6L -> registers[2]
            else -> error("Invalid operand $operand")
        }
    }

}

fun main() {
    Day17().execute()
}

/*

2,4,1,2,7,5,1,3,4,3,5,5,0,3,3,0

2, 4:
reg[B] = reg[A] % 8

1, 2:
reg[B] = reg[B] xor 2

7, 5:
reg[C] = reg[A] % 8

1, 3:
reg[B] = reg[B] xor 3

4, 3:
reg[B] = reg[B] xor reg[C]

5, 5:
output += reg[B] % 8

0, 3:
reg[A] = reg[A] / 8

3, 0:
if (reg[A] != 0) goto 0
 */