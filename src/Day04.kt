fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        val matchedNumbers = matchedNumbers(line)
        if (matchedNumbers.isNotEmpty()) 1.shl(matchedNumbers.size - 1) else 0
    }

    fun part2(input: List<String>): Int {
        val copies = MutableList(input.size) { 1 }
        input.forEachIndexed { cardIndex, line ->
            val matchedNumbers = matchedNumbers(line)
            copies.addCopies(cardIndex, matchedNumbers.size)
        }
        return copies.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun matchedNumbers(line: String): Set<Int> {
    val (_, winningNumbersString, presentNumbersString) = line.split(':', '|')
    val winningNumbers = extractNumbers(winningNumbersString)
    val presentNumbers = extractNumbers(presentNumbersString)
    return presentNumbers.intersect(winningNumbers)
}

fun extractNumbers(s: String): Set<Int> = numberRegex.findAll(s).map { match -> match.value.toInt() }.toSet()

fun MutableList<Int>.addCopies(cardIndex: Int, copiesToAdd: Int) {
    val cardsBelowIndices = cardIndex + 1..cardIndex + copiesToAdd
    cardsBelowIndices.forEach { cardBelowIndex ->
        this[cardBelowIndex] += this[cardIndex]
    }
}
