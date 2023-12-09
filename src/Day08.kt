import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        val instructionSequence = generateInstructionSequence(input.first())
        val nodes = mutableSetOf<Node>()
        lateinit var start: Node
        input.subList(2, input.size).forEach { line ->
            val node = nodes.addNodeFrom(line)
            if (node.name == "AAA") start = node
        }
        return start.stepsToEnd(instructionSequence) { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val instructionSequence = generateInstructionSequence(input.first())
        val nodes = mutableSetOf<Node>()
        val startNodes = buildList {
            input.subList(2, input.size).forEach { line ->
                val node = nodes.addNodeFrom(line)
                if (node.name.endsWith('A')) add(node)
            }
        }
        val steps = startNodes.map { node -> node.stepsToEnd(instructionSequence) { it.endsWith('Z') } }
        return steps.reduce(::lcm)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    //check(part1(testInput) == 2L)
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

fun generateInstructionSequence(instructionLine: String): Sequence<Instruction> = sequence {
    val instructions = parseInstructions(instructionLine)
    while (true) yieldAll(instructions)
}

enum class Instruction { L, R }

fun parseInstructions(s: String): List<Instruction> = s.map { c -> Instruction.valueOf(c.toString()) }

data class Node(val name: String) {
    lateinit var left: Node
    lateinit var right: Node
}

fun MutableSet<Node>.addNodeFrom(s: String): Node {
    val (node, left, right) = nodeNameRegex.findAll(s).map { match -> findOrAdd(match.value) }.toList()
    node.left = left
    node.right = right
    return node
}

val nodeNameRegex = "\\w{3}".toRegex()

fun MutableSet<Node>.findOrAdd(name: String): Node = find { n -> n.name == name } ?: Node(name).also { n -> add(n) }

fun Node.next(instruction: Instruction): Node = when (instruction) {
    Instruction.L -> left
    Instruction.R -> right
}

fun Node.stepsToEnd(instructionSequence: Sequence<Instruction>, endNamePredicate: (String) -> Boolean): Long {
    var currentNode = this
    var steps = 0L
    for (instruction in instructionSequence) {
        steps++
        currentNode = currentNode.next(instruction)
        if (endNamePredicate(currentNode.name)) break
    }
    return steps
}

fun lcm(a: Long, b: Long): Long {
    val largest = max(a, b)
    var lcm = largest
    val maxLcm = a * b
    while (lcm < maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += largest
    }
    return maxLcm
}
