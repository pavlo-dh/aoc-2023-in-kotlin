fun main() {
    fun part1(input: List<String>): Int = input.map(String::parseAsGame).filter(Game::isValid).sumOf(Game::id)

    fun part2(input: List<String>): Int = input.map(String::parseAsGame).map(Game::fewestPossiblePower).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(val id: Int, val cubeSets: List<CubeSet>)

data class CubeSet(val reds: Int, val greens: Int, val blues: Int)

fun String.parseAsGame(): Game {
    val colonIndex = indexOf(':')
    val id = substring(5, colonIndex).toInt()
    val cubeSets = substring(colonIndex + 2).split("; ").map(String::parseAsCubeSet)
    return Game(id, cubeSets)
}

fun String.parseAsCubeSet(): CubeSet {
    val cubeStrings = split(", ")
    val reds = cubesCount(cubeStrings, "red")
    val greens = cubesCount(cubeStrings, "green")
    val blues = cubesCount(cubeStrings, "blue")
    return CubeSet(reds, greens, blues)
}

fun cubesCount(cubeStrings: List<String>, colour: String): Int = cubeStrings.firstNotNullOfOrNull { cubeString ->
    cubeString.substringBefore(" $colour", "").ifEmpty { null }
}?.toInt() ?: 0

fun Game.isValid(): Boolean = cubeSets.all { cubeSet ->
    cubeSet.reds <= 12 &&
            cubeSet.greens <= 13 &&
            cubeSet.blues <= 14
}

fun Game.fewestPossiblePower(): Int =
    cubeSets.maxOf(CubeSet::reds) * cubeSets.maxOf(CubeSet::greens) * cubeSets.maxOf(CubeSet::blues)
