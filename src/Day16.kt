fun main() {
    fun part1(input: List<String>): Int = countEnergizedTiles(Move(Point(0, 0), Direction.RIGHT), input)

    fun part2(input: List<String>): Int {
        val energized = mutableSetOf<Int>()
        for (i in input.indices) {
            energized.add(countEnergizedTiles(Move(Point(i, 0), Direction.RIGHT), input))
            energized.add(countEnergizedTiles(Move(Point(input.lastIndex, i), Direction.UP), input))
            energized.add(countEnergizedTiles(Move(Point(i, input.lastIndex), Direction.LEFT), input))
            energized.add(countEnergizedTiles(Move(Point(0, i), Direction.DOWN), input))
        }
        return energized.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

private fun countEnergizedTiles(startMove: Move, input: List<String>): Int {
    val moves = mutableListOf(startMove)
    val visited = mutableSetOf<Move>()
    val energized = mutableSetOf<Point>()

    while (moves.isNotEmpty()) {
        val move = moves.removeFirst()
        visited.add(move)
        val (point, direction) = move
        energized.add(point)
        val c = input[point.y][point.x]
        val nextDirections = direction.nextDirections(c)
        nextDirections.forEach { nextDirection ->
            point.next(nextDirection, input.indices)?.let { nextPoint ->
                val nextMove = Move(nextPoint, nextDirection)
                if (!visited.contains(nextMove)) moves.add(nextMove)
            }
        }
    }

    return energized.size
}

enum class Direction {
    RIGHT, UP, LEFT, DOWN
}

data class Move(val point: Point, val direction: Direction)

fun Point.next(nextDirection: Direction, range: IntRange): Point? {
    val next = when (nextDirection) {
        Direction.RIGHT -> copy(x = x + 1)
        Direction.UP -> copy(y = y - 1)
        Direction.LEFT -> copy(x = x - 1)
        Direction.DOWN -> copy(y = y + 1)
    }
    return if (next.y in range && next.x in range) next else null
}

private fun Direction.nextDirections(c: Char): List<Direction> = when (c) {
    '/' -> rightTiltedMirror()
    '\\' -> leftTiltedMirror()
    '|' -> verticalSplitter()
    '-' -> horizontalSplitter()
    else -> listOf(this)
}

private fun Direction.rightTiltedMirror(): List<Direction> = when (this) {
    Direction.RIGHT -> listOf(Direction.UP)
    Direction.UP -> listOf(Direction.RIGHT)
    Direction.LEFT -> listOf(Direction.DOWN)
    Direction.DOWN -> listOf(Direction.LEFT)
}

private fun Direction.leftTiltedMirror(): List<Direction> = when (this) {
    Direction.RIGHT -> listOf(Direction.DOWN)
    Direction.UP -> listOf(Direction.LEFT)
    Direction.LEFT -> listOf(Direction.UP)
    Direction.DOWN -> listOf(Direction.RIGHT)
}

private fun Direction.verticalSplitter(): List<Direction> = when (this) {
    Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
    Direction.UP -> listOf(this)
    Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
    Direction.DOWN -> listOf(this)
}

private fun Direction.horizontalSplitter(): List<Direction> = when (this) {
    Direction.RIGHT -> listOf(this)
    Direction.UP -> listOf(Direction.RIGHT, Direction.LEFT)
    Direction.LEFT -> listOf(this)
    Direction.DOWN -> listOf(Direction.RIGHT, Direction.LEFT)
}
