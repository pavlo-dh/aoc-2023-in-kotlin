fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        val first = line.first(Char::isDigit)
        val last = line.last(Char::isDigit)
        "$first$last".toInt()
    }

    fun part2(input: List<String>): Int = input.sumOf { line ->
        val first = firstDigit(line)
        val last = lastDigit(line)
        "$first$last".toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    //check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun firstDigit(line: String): Char {
    val (digit, _) = Digit.entries.associateWith { digit ->
        line.indexOf(digit.representation)
    }.filterValues { index ->
        index != -1
    }.minBy { (_, index) ->
        index
    }
    return digit.char
}

fun lastDigit(line: String): Char {
    val (digit, _) = Digit.entries.associateWith { digit ->
        line.lastIndexOf(digit.representation)
    }.maxBy { (_, index) ->
        index
    }
    return digit.char
}

enum class Digit(val representation: String, val char: Char) {
    ZERO("0", '0'),
    ONE("1", '1'),
    TWO("2", '2'),
    THREE("3", '3'),
    FOUR("4", '4'),
    FIVE("5", '5'),
    SIX("6", '6'),
    SEVEN("7", '7'),
    EIGHT("8", '8'),
    NINE("9", '9'),
    ONE_SPELLED("one", '1'),
    TWO_SPELLED("two", '2'),
    THREE_SPELLED("three", '3'),
    FOUR_SPELLED("four", '4'),
    FIVE_SPELLED("five", '5'),
    SIX_SPELLED("six", '6'),
    SEVEN_SPELLED("seven", '7'),
    EIGHT_SPELLED("eight", '8'),
    NINE_SPELLED("nine", '9'),
}
