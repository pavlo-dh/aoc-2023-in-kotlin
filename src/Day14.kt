fun main() {
    fun part1(input: List<String>): Int {
        val i = input.map { it.toMutableList() }
        i.tiltNorth()
        return i.mapIndexed { index, row ->
            row.count { it == 'O' } * (i.size - index)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val i = input.map { it.toMutableList() }
        var firstMatch = -1
        var cycles = 0
        val states = mutableListOf<List<List<Char>>>()
        do {
            i.spin()
            states.add(i.map { it.toList() })
            val match = states.withIndex().firstOrNull {
                it.index < cycles && it.value == i
            }
            if (match != null) {
                firstMatch = cycles
                break
            }
            cycles++
        } while (firstMatch == -1)
        val repetitions = mutableSetOf(states.last())
        var previousSize = 0
        do {
            previousSize++
            i.spin()
            repetitions.add(i.map { it.toList() })
        } while (repetitions.size != previousSize)
        val repetition = (1000000000 - firstMatch - 1) % repetitions.size
        return repetitions.toList()[repetition].mapIndexed { index, row ->
            row.count { it == 'O' } * (i.size - index)
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

private fun List<MutableList<Char>>.spin() {
    this.tiltNorth()
    this.tiltWest()
    this.tiltSouth()
    this.tiltEast()
}

private fun List<MutableList<Char>>.tiltNorth() {
    val xIndices = get(0).indices
    for (y in 1..lastIndex) {
        for (x in xIndices) {
            for (k in y - 1 downTo 0) {
                val current = get(k + 1)[x]
                val upper = get(k)[x]
                if (current == 'O' && upper == '.') {
                    get(k)[x] = 'O'
                    get(k + 1)[x] = '.'
                } else {
                    break
                }
            }
        }
    }
}

private fun List<MutableList<Char>>.tiltSouth() {
    val xIndices = get(0).indices
    for (y in lastIndex - 1 downTo 0) {
        for (x in xIndices) {
            for (k in y + 1..lastIndex) {
                val current = get(k - 1)[x]
                val lower = get(k)[x]
                if (current == 'O' && lower == '.') {
                    get(k)[x] = 'O'
                    get(k - 1)[x] = '.'
                } else {
                    break
                }
            }
        }
    }
}

private fun List<MutableList<Char>>.tiltWest() {
    val xLast = get(0).lastIndex
    for (x in 1..xLast) {
        for (y in indices) {
            for (k in x - 1 downTo 0) {
                val current = get(y)[k + 1]
                val upper = get(y)[k]
                if (current == 'O' && upper == '.') {
                    get(y)[k] = 'O'
                    get(y)[k + 1] = '.'
                } else {
                    break
                }
            }
        }
    }
}

private fun List<MutableList<Char>>.tiltEast() {
    val xLast = get(0).lastIndex
    for (x in xLast - 1 downTo 0) {
        for (y in indices) {
            for (k in x + 1..xLast) {
                val current = get(y)[k - 1]
                val upper = get(y)[k]
                if (current == 'O' && upper == '.') {
                    get(y)[k] = 'O'
                    get(y)[k - 1] = '.'
                } else {
                    break
                }
            }
        }
    }
}
