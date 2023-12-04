fun main() {
    fun part1(input: List<String>): Int {
        val symbolLocations = findSymbolLocations(input) { char -> !char.isDigit() && char != '.' }
        return input.withIndex().sumOf { (lineIndex, line) ->
            numberRegex.findAll(line).filter { matchResult ->
                symbolLocations.any { location -> location.isAdjacentTo(lineIndex, matchResult.range) }
            }.map { matchResult ->
                matchResult.value.toInt()
            }.sum()
        }
    }

    fun part2(input: List<String>): Int {
        val gearLocations = findSymbolLocations(input) { char -> char == '*' }
        val gearLocationsToAdjacentNumbers = gearLocations.associateWith { mutableListOf<Int>() }
        input.forEachIndexed { lineIndex, line ->
            numberRegex.findAll(line).forEach { matchResult ->
                gearLocations.forEach { location ->
                    if (location.isAdjacentTo(lineIndex, matchResult.range)) {
                        gearLocationsToAdjacentNumbers[location]?.add(matchResult.value.toInt())
                    }
                }
            }
        }
        return gearLocationsToAdjacentNumbers.values.filter { adjacentNumbers ->
            adjacentNumbers.size == 2
        }.sumOf { adjacentNumbers ->
            adjacentNumbers.first() * adjacentNumbers.last()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class Location(val y: Int, val x: Int)

fun findSymbolLocations(input: List<String>, predicate: (Char) -> Boolean): List<Location> =
    input.flatMapIndexed { lineIndex, line ->
        line.mapIndexedNotNull { charIndex, char ->
            if (predicate(char)) Location(lineIndex, charIndex) else null
        }
    }

fun Location.isAdjacentTo(y: Int, xRange: IntRange): Boolean =
    this.y in (y - 1)..(y + 1) && this.x in (xRange.first - 1)..(xRange.last + 1)
