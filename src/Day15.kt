fun main() {
    fun part1(input: List<String>): Long {
        val result = input.first().split(',').sumOf(::hash)
        return result
    }

    fun part2(input: List<String>): Long {
        val boxes = (0L..255L).associateWith { mutableMapOf<String, Int>() }
        val labelRegex = "\\w+".toRegex()
        input.first().split(',').forEach { step ->
            val match = labelRegex.find(step) ?: throw Exception("No match found!")
            val label = match.value
            val boxNumber = hash(label)
            val operationIndex = match.range.last + 1
            val operation = step[operationIndex]
            when (operation) {
                '=' -> {
                    val focalLength = step.substring(operationIndex + 1).toInt()
                    boxes.getValue(boxNumber)[label] = focalLength
                }

                '-' -> boxes.getValue(boxNumber).remove(label)
            }
        }
        var answer = 0L
        for ((boxNumber, box) in boxes) {
            box.onEachIndexed { slotNumber, (_, focalLength) ->
                val focusingPower = (boxNumber + 1L) * (slotNumber + 1L) * focalLength
                answer += focusingPower
            }
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)
    check(part2(testInput) == 145L)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

private fun hash(s: String): Long {
    val hash = s.fold(0L) { hash, char ->
        (hash + char.code) * 17 % 256
    }
    return hash
}
