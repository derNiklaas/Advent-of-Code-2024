import utils.AoCDay
import utils.chunkedInput
import utils.splitAndMapToInt

class Day05 : AoCDay() {

    val order: List<Pair<Int, Int>>
    val printingOrder: List<List<Int>>

    init {
        val parts = input.chunkedInput()
        order = parts[0].map {
            val splits = it.splitAndMapToInt("|")
            splits[0] to splits[1]
        }
        printingOrder = parts[1].splitAndMapToInt(",")
    }

    override fun part1(): Any {
        var count = 0L
        printingOrder.forEach { order ->
            if (isCorrectOrder(order)) {
                count += order[order.size / 2]
            }
        }
        return count
    }

    override fun part2(): Any {
        val incorrect = printingOrder.filter { !isCorrectOrder(it) }
        val fixed = incorrect.mapNotNull { fixOrder(it.size * 2, it) }
        var count = 0L
        fixed.forEach { order ->
            if (isCorrectOrder(order)) {
                count += order[order.size / 2]
            }
        }
        return count
    }

    private fun isCorrectOrder(order: List<Int>): Boolean {
        val printed = mutableSetOf<Int>()
        order.forEach { page ->

            for (orders in this.order) {
                if (orders.first in order && orders.second in order && orders.second == page) {
                    if (orders.first !in printed) {
                        return false
                    }
                }
            }
            printed += page
        }
        return true
    }

    private fun fixOrder(triesLeft: Int, order: List<Int>): List<Int>? {
        if (isCorrectOrder(order)) return order
        if (triesLeft == 0) return null
        val printed = mutableSetOf<Int>()
        order.forEach { page ->

            for (orders in this.order) {
                if (orders.first in order && orders.second in order && orders.second == page) {
                    if (orders.first !in printed) {
                        val indexBefore = order.indexOf(orders.first)
                        val indexAfter = order.indexOf(orders.second)
                        val newOrder = order.toMutableList()
                        newOrder.removeAt(indexBefore)
                        newOrder.add(indexAfter, orders.first)
                        return fixOrder(triesLeft - 1, newOrder)
                    }
                }
            }
            printed += page
        }
        return null
    }
}

fun main() {
    Day05().execute()
}
