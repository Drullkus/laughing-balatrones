package us.drullk

// Returns a list with counts of each distinct rank
fun rankCounts(hand: List<PokerCard>): Collection<Int> =
    hand.groupingBy { it.rank }.eachCount().values

/**
 * A generator function generating
 */
fun pokerDeckGenerator(): Sequence<PokerCard> = sequence {
    for (suit in Suit.entries) {
        for (rank in Rank.entries) {
            yield(PokerCard(suit, rank))
        }
    }
}

/**
 * Generates all combinations of a list taken r at a time as a Sequence.
 */
fun <T> combinations(list: List<T>, r: Int): Sequence<List<T>> = sequence {
    val n = list.size
    if (r > n || r < 0) return@sequence
    val indices = IntArray(r)
    while (true) {
        yield(indices.map(list::get))
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

fun longestConsecutiveSequence(nums: List<Int>, shortcut: Boolean = false): Int {
    if (nums.isEmpty()) return 0

    var longest = 1
    var current = 1

    for (i in 1 until nums.size) {
        val diff = nums[i] - nums[i - 1]
        if (!shortcut) {
            if (diff == 1) {
                current++
            } else {
                longest = maxOf(longest, current)
                current = 1
            }
        } else {
            // When allowing a gap, we treat a difference of 1 or 2 as consecutive.
            if (diff in 1..2) {
                current++
            } else {
                longest = maxOf(longest, current)
                current = 1
            }
        }
    }
    return maxOf(longest, current)
}

/**
 * Doesn't actually create what's structurally a vector, but it is still a vector in consideration of each value-component being assigned
 */
fun vectorizeDeckChances(deck: List<PokerCard>): Map<PokerHand, Double> {
    val results = HashMap<PokerHand, Double>()

    for (hand in PokerHand.entries)
        results[hand] = handPercentile(hand.predicate, deck)

    return results
}