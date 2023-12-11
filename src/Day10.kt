fun main() {
    fun part1(input: List<String>): Int {
        val loop = input.findLoop()
        return loop.size / 2
    }

    fun part2(input: List<String>): Int {
        val loop = input.findLoop()
        return input.countTilesEnclosedWithin(loop)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    //check(part1(testInput) == 8)
    check(part2(testInput) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

fun List<String>.findLoop(): List<Point> {
    val start = findStart()
    val maxY = lastIndex
    val maxX = first().lastIndex
    val neighbours = start.neighbours().filter { it.isOnGrid(maxY, maxX) }
    return buildLoop(start, neighbours.first { start in connectionsFrom(it) })
}

data class Point(val y: Int, val x: Int)

fun List<String>.findStart(): Point {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == 'S') return Point(y, x)
        }
    }
    throw Exception("Starting position not found!")
}

fun Point.neighbours(): List<Point> = listOf(-1, 1).fold(emptyList()) { neighbours, delta ->
    neighbours + Point(y + delta, x) + Point(y, x + delta)
}

fun Point.isOnGrid(maxY: Int, maxX: Int): Boolean = y in 0..maxY && x in 0..maxX

fun List<String>.buildLoop(first: Point, second: Point): List<Point> {
    val loop = mutableListOf(first)
    var previous = first
    var current = second
    while (current != first) {
        loop.add(current)
        val next = connectionsFrom(current).minus(previous).first()
        previous = current
        current = next
    }
    return loop
}

fun List<String>.connectionsFrom(point: Point): List<Point> = tileAt(point).connectionsFrom(point)

fun List<String>.tileAt(point: Point) = this[point.y][point.x].toTile()

fun Char.toTile(): Tile = when (this) {
    '|' -> Tile.VerticalPipe
    '-' -> Tile.HorizontalPipe
    'L' -> Tile.NorthEastBend
    'J' -> Tile.NorthWestBend
    '7' -> Tile.SouthWestBend
    'F' -> Tile.SouthEastBend
    else -> Tile.Ground
}

enum class Tile {
    VerticalPipe, HorizontalPipe, NorthEastBend, NorthWestBend, SouthWestBend, SouthEastBend, Ground
}

fun Tile.connectionsFrom(point: Point): List<Point> = when (this) {
    Tile.VerticalPipe -> listOf(Point(point.y + 1, point.x), Point(point.y - 1, point.x))
    Tile.HorizontalPipe -> listOf(Point(point.y, point.x + 1), Point(point.y, point.x - 1))
    Tile.NorthEastBend -> listOf(Point(point.y - 1, point.x), Point(point.y, point.x + 1))
    Tile.NorthWestBend -> listOf(Point(point.y - 1, point.x), Point(point.y, point.x - 1))
    Tile.SouthWestBend -> listOf(Point(point.y, point.x - 1), Point(point.y + 1, point.x))
    Tile.SouthEastBend -> listOf(Point(point.y, point.x + 1), Point(point.y + 1, point.x))
    Tile.Ground -> emptyList()
}

fun List<String>.countTilesEnclosedWithin(loop: List<Point>): Int {
    val start = loop.first()
    val startTile = loop.determineStartTile()
    var count = 0
    forEachIndexed { y, line ->
        var inside = false
        var previousTile: Tile? = null
        line.forEachIndexed { x, _ ->
            val point = Point(y, x)
            if (loop.contains(point)) {
                val tile = if (point == start) startTile else tileAt(point)
                inside = if (tile == Tile.VerticalPipe) {
                    !inside
                } else if (tile == Tile.NorthEastBend) {
                    previousTile = tile
                    inside
                } else if (tile == Tile.SouthWestBend && previousTile == Tile.NorthEastBend) {
                    !inside
                } else if (tile == Tile.SouthEastBend) {
                    previousTile = tile
                    inside
                } else if (tile == Tile.NorthWestBend && previousTile == Tile.SouthEastBend) {
                    !inside
                } else {
                    inside
                }
            } else if (inside) {
                count++
            }
        }
    }
    return count
}

fun List<Point>.determineStartTile(): Tile = Tile.entries.first { tile ->
    tile.connectionsFrom(first()).toSet() == setOf(get(1), last())
}
