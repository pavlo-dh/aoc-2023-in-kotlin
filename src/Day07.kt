@file:Suppress("EnumEntryName")

fun main() {
    fun part1(input: List<String>): Long {
        val hands = input.map(String::toHand)
        val sortedHands = hands.sortedWith(Comparator.comparing(Hand::type).thenComparing { hand -> hand.cards.first() }
            .thenComparing { hand -> hand.cards[1] }.thenComparing { hand -> hand.cards[2] }
            .thenComparing { hand -> hand.cards[3] }.thenComparing { hand -> hand.cards.last() }.reversed())
        return sortedHands.withIndex().sumOf { (index, hand) ->
            hand.bid * (index + 1)
        }
    }

    fun part2(input: List<String>): Long {
        val hands = input.map(String::toJHand)
        val sortedHands =
            hands.sortedWith(Comparator.comparing(JHand::type).thenComparing { hand -> hand.cards.first() }
                .thenComparing { hand -> hand.cards[1] }.thenComparing { hand -> hand.cards[2] }
                .thenComparing { hand -> hand.cards[3] }.thenComparing { hand -> hand.cards.last() }.reversed())
        return sortedHands.withIndex().sumOf { (index, hand) ->
            hand.bid * (index + 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class Card {
    A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`
}

data class Hand(val cards: List<Card>, val bid: Long)

fun String.toHand(): Hand = with(this.split(" ")) {
    val cards = first().map { Card.valueOf(it.toString()) }
    val bid = last().toLong()
    Hand(cards, bid)
}

enum class Type {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard
}

fun Hand.type(): Type = when {
    isFiveOfAKind() -> Type.FiveOfAKind
    isFourOfAKind() -> Type.FourOfAKind
    isFullHouse() -> Type.FullHouse
    isThreeOfAKind() -> Type.ThreeOfAKind
    isTwoPair() -> Type.TwoPair
    isOnePair() -> Type.OnePair
    else -> Type.HighCard
}

fun Hand.isFiveOfAKind() = cards.toSet().size == 1

fun Hand.isFourOfAKind() = cards.groupBy { it }.any { it.value.size == 4 }

fun Hand.isFullHouse() =
    cards.groupBy { it }.let { map -> map.any { it.value.size == 3 } && map.any { it.value.size == 2 } }

fun Hand.isThreeOfAKind() = cards.groupBy { it }.any { it.value.size == 3 }

fun Hand.isTwoPair() = cards.groupBy { it }.let { map -> 2 == map.count { it.value.size == 2 } }

fun Hand.isOnePair() = cards.toSet().size == 4

enum class JCard {
    A, K, Q, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`, J
}

data class JHand(val cards: List<JCard>, val bid: Long)

fun String.toJHand(): JHand = with(this.split(" ")) {
    val cards = first().map { JCard.valueOf(it.toString()) }
    val bid = last().toLong()
    JHand(cards, bid)
}

fun JHand.type(): Type = when {
    isFiveOfAKind() -> Type.FiveOfAKind
    isFourOfAKind() -> Type.FourOfAKind
    isFullHouse() -> Type.FullHouse
    isThreeOfAKind() -> Type.ThreeOfAKind
    isTwoPair() -> Type.TwoPair
    isOnePair() -> Type.OnePair
    else -> Type.HighCard
}

fun JHand.isFiveOfAKind(): Boolean {
    val set = cards.toSet()
    return set.size == 1 || set.contains(JCard.J) && set.size == 2
}

fun JHand.isFourOfAKind(): Boolean {
    val map = cards.groupBy { it }
    val jCount = map.jCount()
    return map.filterJ().any { it.value.size + jCount == 4 }
}

fun JHand.isFullHouse(): Boolean {
    val map = cards.groupBy { it }
    val jCount = map.jCount()
    return map.filterJ().values.map(List<JCard>::size).sortedDescending().take(2).sum() + jCount == 5
}

fun JHand.isThreeOfAKind(): Boolean {
    val map = cards.groupBy { it }
    val jCount = map.jCount()
    return map.filterJ().any { it.value.size + jCount == 3 }
}

fun JHand.isTwoPair(): Boolean = cards.groupBy { it }.let { map -> 2 == map.count { it.value.size == 2 } }

fun JHand.isOnePair(): Boolean {
    val set = cards.toSet()
    return set.size == 4 || set.contains(JCard.J) && set.size == 5
}

fun Map<JCard, List<JCard>>.jCount(): Int = this[JCard.J]?.size ?: 0

fun Map<JCard, List<JCard>>.filterJ(): Map<JCard, List<JCard>> = filterKeys { it != JCard.J }
