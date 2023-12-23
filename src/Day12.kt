import java.math.BigInteger
import java.util.BitSet

fun main() {
    fun part1(input: List<String>): Int {
        val answer = input.sumOf { line ->
            val (row, groupSizesString) = line.split(' ')
            val groupSizes = numberRegex.findAll(groupSizesString).map { it.value.toInt() }.toList()
            var count = 0
            val unknowns = mutableListOf<Int>()
            row.forEachIndexed { index, c -> if (c == '?') unknowns.add(index) }
            val possibilities = 1.shl(unknowns.size)
            for (i in 0..<possibilities) {
                val bitSet = BitSet.valueOf(longArrayOf(i.toLong()))
                val attempt = row.toMutableList()
                for (j in 0..<bitSet.length()) {
                    val b = bitSet[j]
                    if (b) attempt[unknowns[j]] = '#'
                }
                attempt.replaceAll { c -> if (c == '?') '.' else c }
                if (attempt.isValidAccordingTo(groupSizes)) count++
            }
            count
        }
        return answer
    }

    fun part2(input: List<String>): BigInteger {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    //check(part2(testInput) == BigInteger.valueOf(525152L))

    val input = readInput("Day12")
    part1(input).println()
    //part2(input).println()
}

fun List<Char>.isValidAccordingTo(groupSizes: List<Int>): Boolean {
    val groups = String(toCharArray()).split('.').filter(String::isNotEmpty)
    if (groups.size != groupSizes.size) return false
    groups.forEachIndexed { index, group -> if (group.length != groupSizes[index]) return false }
    return true
}
