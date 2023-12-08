fun main() {
    fun part1(input: List<String>): Long {
        val times = numberRegex.findAll(input.first()).map { it.value.toLong() }
        val distances = numberRegex.findAll(input.last()).map { it.value.toLong() }
        val races = times.zip(distances) { time, distance -> Race(time, distance) }
        return races.map(Race::waysToWin).reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val time =
            numberRegex.findAll(input.first()).joinToString(separator = "", transform = MatchResult::value).toLong()
        val distance =
            numberRegex.findAll(input.last()).joinToString(separator = "", transform = MatchResult::value).toLong()
        return Race(time, distance).waysToWin()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

data class Race(val time: Long, val distance: Long)

fun Race.waysToWin(): Long = (1..<time).filter { t -> (time - t) * t > distance }.size.toLong()
