fun main() {
    fun part1(input: List<String>): Int {
        val patterns = findPatterns(input)
        return patterns.sumOf { pattern ->
            symmetryValue(pattern)
        }
    }

    fun part2(input: List<String>): Int {
        val patterns = findPatterns(input)
        val answer = patterns.sumOf { pattern ->
            symmetryValue2(pattern)
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

fun findPatterns(input: List<String>): List<List<String>> {
    val emptyLineIndices = input.withIndex().filter { (_, line) -> line.isEmpty() }.map(IndexedValue<String>::index)
    val patterns = mutableListOf<List<String>>()
    patterns.add(input.subList(0, emptyLineIndices.first()))
    emptyLineIndices.zipWithNext().forEach { (a, b) ->
        patterns.add(input.subList(a + 1, b))
    }
    patterns.add(input.subList(emptyLineIndices.last() + 1, input.size))
    return patterns
}

fun symmetryValue(pattern: List<String>): Int {
    var answer = 0
    val rows = pattern.size
    val columns = pattern.first().length
    for (c in 0..<columns - 1) {
        var ok = true
        for (dc in 0..<columns) {
            val left = c - dc
            val right = c + 1 + dc
            if (left in 0..<right && right in left + 1..<columns) {
                for (r in 0..<rows) {
                    if (pattern[r][left] != pattern[r][right]) {
                        ok = false
                    }
                }
            }
        }
        if (ok) {
            answer += c + 1
            break
        }
    }
    for (r in 0..<rows - 1) {
        var ok = true
        for (dr in 0..<rows) {
            val up = r - dr
            val down = r + 1 + dr
            if (up in 0..<down && down in up + 1..<rows) {
                for (c in 0..<columns) {
                    if (pattern[up][c] != pattern[down][c]) {
                        ok = false
                    }
                }
            }
        }
        if (ok) {
            answer += 100 * (r + 1)
            break
        }
    }
    return answer
}

fun symmetryValue2(pattern: List<String>): Int {
    var answer = 0
    val rows = pattern.size
    val columns = pattern.first().length
    for (c in 0..<columns - 1) {
        var diffs = 0
        for (dc in 0..<columns) {
            val left = c - dc
            val right = c + 1 + dc
            if (left in 0..<right && right in left + 1..<columns) {
                for (r in 0..<rows) {
                    if (pattern[r][left] != pattern[r][right]) {
                        diffs++
                    }
                }
            }
        }
        if (diffs == 1) {
            answer += c + 1
            break
        }
    }
    for (r in 0..<rows - 1) {
        var diffs = 0
        for (dr in 0..<rows) {
            val up = r - dr
            val down = r + 1 + dr
            if (up in 0..<down && down in up + 1..<rows) {
                for (c in 0..<columns) {
                    if (pattern[up][c] != pattern[down][c]) {
                        diffs++
                    }
                }
            }
        }
        if (diffs == 1) {
            answer += 100 * (r + 1)
            break
        }
    }
    return answer
}
