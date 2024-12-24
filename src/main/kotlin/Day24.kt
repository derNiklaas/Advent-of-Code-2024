import utils.AoCDay
import utils.chunkedInput

class Day24 : AoCDay() {
    val chunks = input.chunkedInput()

    val states = chunks.first().map {
        val (name, state) = it.split(": ")
        name to (state == "1")
    }.toMap().toMutableMap()

    val gates = chunks.last().map {
        val (gates, output) = it.split(" -> ")
        val (a, instruction, b) = it.split(" ")
        when (instruction) {
            "AND" -> Gate.AndGate(a, b, output)
            "OR" -> Gate.OrGate(a, b, output)
            "XOR" -> Gate.XorGate(a, b, output)
            else -> throw IllegalArgumentException("Unknown instruction: $instruction")
        }
    }

    override fun part1(): Any {
        val states = this.states.toMutableMap()
        return executeProgram(gates, states)
    }

    override fun part2(): Any {
        val states = this.states.toMutableMap()
        val gates = this.gates.toMutableList()

        val inputPairs = states.keys.sorted().partition { it.startsWith("x") }.let { (a, b) -> a.zip(b) }

        val xor1 = inputPairs.map { (x, y) ->
            gates.first { (it.a == x || it.b == x) && (it.a == y || it.b == y) && it is Gate.XorGate }.output
        }

        val and1 = inputPairs.map { (x, y) ->
            gates.first { (it.a == x || it.b == x) && (it.a == y || it.b == y) && it is Gate.AndGate }.output
        }

        val outputs = gates.filter { it.output.startsWith("z") && it is Gate.XorGate }.sortedBy { it.output }

        fun findOr(input: String) = gates.first { it ->
            (it.a == input || it.b == input) && it is Gate.OrGate
        }

        fun findAnd(input: String) = gates.first { it ->
            (it.a == input || it.b == input) && it is Gate.AndGate
        }

        fun findXor(input: String) = gates.first { it ->
            (it.a == input || it.b == input) && it is Gate.XorGate
        }

        val incorrectOutputs =
            (0..44).map { "z" + it.toString().padStart(2, '0') }.filter { it !in outputs.map { it.output } }

        val incorrectOutputPairs = incorrectOutputs.map { it.drop(1).toInt() }.map { findXor(xor1[it]).output }

        var incorrect = (incorrectOutputs + incorrectOutputPairs).toMutableList()

        for (i in 1..44) {
            val out1 = and1[i]
            val out2 = try {
                findOr(out1).output
            } catch (_: NoSuchElementException) {
                continue
            }
            val out3 = try {
                findAnd(out2).output
            } catch (_: NoSuchElementException) {
                continue
            }
            val orGate = try {
                findOr(out3)
            } catch (_: NoSuchElementException) {
                continue
            }

            val input1 = and1[i + 1]
            val next = try {
                findOr(input1)
            } catch (_: NoSuchElementException) {
                null
            }

            if (next == null) {
                if (input1 in incorrectOutputs) continue

                incorrect += input1
                incorrect += listOf(orGate.a, orGate.b).filter { it != out3 }
            }
        }

        return incorrect.sorted().joinToString(",")
    }

    fun executeProgram(gates: List<Gate>, states: MutableMap<String, Boolean>): Long {
        val queue = gates.toMutableList()
        while (queue.isNotEmpty()) {
            val gate = queue.removeFirst()
            if (gate.a in states && gate.b in states) {
                gate.eval(states)
            } else {
                queue += gate
            }
        }

        return states.filter { it.key.startsWith("z") }.toSortedMap().values.joinToString("") {
            if (it) "1" else "0"
        }.toLong(2)
    }

    sealed interface Gate {
        val a: String
        val b: String
        val output: String
        fun eval(states: MutableMap<String, Boolean>)

        data class AndGate(override val a: String, override val b: String, override val output: String) : Gate {
            override fun eval(states: MutableMap<String, Boolean>) {
                val output = states[a]!! && states[b]!!
                states[this.output] = output
            }
        }

        data class OrGate(override val a: String, override val b: String, override val output: String) : Gate {
            override fun eval(states: MutableMap<String, Boolean>) {
                val output = states[a]!! || states[b]!!
                states[this.output] = output
            }
        }

        data class XorGate(override val a: String, override val b: String, override val output: String) : Gate {
            override fun eval(states: MutableMap<String, Boolean>) {
                val output = states[a]!! xor states[b]!!
                states[this.output] = output
            }
        }
    }
}

fun main() {
    Day24().execute()
}
