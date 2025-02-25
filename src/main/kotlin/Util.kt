package us.drullk

val hands: Map<String, (List<PokerCard>) -> Boolean> = mapOf(
    "High Card" to ::isHighCard,
    "Pair" to ::isPair,
    "Two Pair" to ::isTwoPair,
    "Three of a Kind" to ::isThreeOfAKind,
    "Straight" to ::isStraight,
    "Flush" to ::isFlush,
    "Full House" to ::isFullHouse,
    "Four of a Kind" to ::isFourOfAKind,
    "Straight Flush" to ::isStraightFlush,
    "Royal Flush" to ::isRoyalFlush,
    "Five of a Kind" to ::isFiveOfAKind,
    "Flush House" to ::isFlushHouse,
    "Flush Five" to ::isFlushFive
)

// Returns a map with counts of each rank.
fun rankCounts(hand: List<PokerCard>): Map<Rank, Int> =
    hand.groupingBy { it.rank }.eachCount()

fun cardGenerator(): List<PokerCard> =
    Suit.entries.flatMap { suit -> Rank.entries.map { rank -> PokerCard(suit, rank) } }

/**
 * Generates all combinations of a list taken r at a time as a Sequence.
 */
fun <T> combinations(list: List<T>, r: Int): Sequence<List<T>> = sequence {
    val n = list.size
    if (r > n || r < 0) return@sequence
    val indices = IntArray(r) { it }
    while (true) {
        yield(indices.map { list[it] })
        var i = r - 1
        while (i >= 0 && indices[i] == i + n - r) {
            i--
        }
        if (i < 0) break
        indices[i]++
        for (j in i + 1 until r) {
            indices[j] = indices[j - 1] + 1
        }
    }
}

/**
 * Computes n choose r.
 */
fun comb(n: Int, r: Int): Long {
    var result = 1L
    for (i in 1..r) {
        result = result * (n - i + 1) / i
    }
    return result
}

/**
 * Calculates the percentage of all 5-card hands from a given deck
 * that satisfy the given predicate.
 */
fun handPercentile(predicate: (List<PokerCard>) -> Boolean, deck: List<PokerCard>): Double {
    val totalHands = comb(deck.size, 5)
    var validHands = 0L
    for (hand in combinations(deck, 5)) {
        if (predicate(hand)) validHands++
    }
    return (validHands.toDouble() / totalHands.toDouble()) * 100.0
}