fun main() {
    fun part1(input: List<String>): Long = input.sumOf { line ->
        line.extrapolatedValue(List<Long>::last, ExtrapolationMode.FORWARDS)
    }

    fun part2(input: List<String>): Long = input.sumOf { line ->
        line.extrapolatedValue(List<Long>::first, ExtrapolationMode.BACKWARDS)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun String.extrapolatedValue(valueGetter: (List<Long>) -> Long, extrapolationMode: ExtrapolationMode): Long {
    val history = parseAsHistory()
    val valuesForExtrapolation = getValuesForExtrapolation(history, valueGetter)
    return getExtrapolatedValue(valuesForExtrapolation, extrapolationMode)
}

fun String.parseAsHistory() = split(" ").map(String::toLong)

fun getValuesForExtrapolation(history: List<Long>, valueGetter: (List<Long>) -> Long): List<Long> =
    generateSequence(history) { sequence ->
        sequence.zipWithNext { first, second -> second - first }
    }.takeWhile { sequence ->
        sequence.any { it != 0L }
    }.map { sequence ->
        valueGetter(sequence)
    }.toList()

fun getExtrapolatedValue(valuesForExtrapolation: List<Long>, extrapolationMode: ExtrapolationMode): Long =
    valuesForExtrapolation.reversed().fold(0L) { newValue, v ->
        when (extrapolationMode) {
            ExtrapolationMode.FORWARDS -> newValue + v
            ExtrapolationMode.BACKWARDS -> v - newValue
        }
    }

enum class ExtrapolationMode { FORWARDS, BACKWARDS }
