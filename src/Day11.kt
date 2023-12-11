import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): BigInteger {
        val locations = input.findGalaxyLocations()
        val pairs = locations.createPairs()
        return pairs.sumOf { (first, second) -> input.distanceBetween(first, second, BigInteger.ONE) }
    }

    fun part2(input: List<String>): BigInteger {
        val locations = input.findGalaxyLocations()
        val pairs = locations.createPairs()
        return pairs.sumOf { (first, second) -> input.distanceBetween(first, second, BigInteger.valueOf(999999L)) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == BigInteger.valueOf(374))
    //check(part2(testInput) == BigInteger.valueOf(8410))

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

fun List<String>.findGalaxyLocations(): List<Point> {
    val columnIndices = first().indices
    val locations = mutableListOf<Point>()
    for (y in indices) {
        for (x in columnIndices) {
            if (get(y)[x] == '#') {
                locations.add(Point(y, x))
            }
        }
    }
    return locations
}

fun List<Point>.createPairs(): List<Pair<Point, Point>> {
    val pairs = mutableListOf<Pair<Point, Point>>()
    for (i in indices) {
        for (j in i + 1..lastIndex) {
            pairs.add(get(i) to get(j))
        }
    }
    return pairs
}

fun List<String>.distanceBetween(first: Point, second: Point, addedCopies: BigInteger): BigInteger {
    val minRow = min(first.y, second.y)
    val maxRow = max(first.y, second.y)
    val minColumn = min(first.x, second.x)
    val maxColumn = max(first.x, second.x)
    val extraRows = extraRows(minRow, maxRow, addedCopies)
    val extraColumns = extraColumns(minColumn, maxColumn, addedCopies)
    return (minRow.toBigInteger() - (maxRow.toBigInteger() + extraRows)).abs() +
            (minColumn.toBigInteger() - (maxColumn.toBigInteger() + extraColumns)).abs()
}

fun List<String>.extraRows(minRow: Int, maxRow: Int, addedCopies: BigInteger): BigInteger {
    var extraRows = BigInteger.ZERO
    for (r in minRow + 1..<maxRow) {
        if (!get(r).contains('#')) extraRows += addedCopies
    }
    return extraRows
}

fun List<String>.extraColumns(minColumn: Int, maxColumn: Int, addedCopies: BigInteger): BigInteger {
    var extraColumns = BigInteger.ZERO
    for (c in minColumn + 1..<maxColumn) {
        var containsGalaxy = false
        for (r in indices) {
            if (get(r)[c] == '#') {
                containsGalaxy = true
                break
            }
        }
        if (!containsGalaxy) extraColumns += addedCopies
    }
    return extraColumns
}
