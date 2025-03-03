package us.drullk

/**
 * Poker hands valid in Balatro: https://balatrogame.fandom.com/wiki/Poker_Hands
 *
 * Enum ordered by check precedence
 *
 * - Four Fingers Joker: "All Flushes and Straights can be made with 4 cards" https://balatrogame.fandom.com/wiki/Four_Fingers
 * - Shortcut Joker: "Allows Straights to be made with gaps of 1 rank (ex: 10 8 6 5 3)" https://balatrogame.fandom.com/wiki/Shortcut
 */
enum class PokerHand(val displayName: String, val baseChips: Int, val baseMult: Int, val predicate: (List<PokerCard>) -> Boolean) {
    FLUSH_FIVE("Flush Five", 160, 16, ::hasFlushFive),
    FLUSH_HOUSE("Flush House", 140, 14, ::hasFlushHouse),
    FIVE_OF_A_KIND("Five of a Kind", 120, 12, ::hasFiveOfAKind),
    ROYAL_FLUSH("Royal Flush", 100, 8, ::hasRoyalFlush),
    STRAIGHT_FLUSH("Straight Flush", 100, 8, ::hasStraightFlush),
    FOUR_OF_A_KIND("Four of a Kind", 60, 7, ::hasFourOfAKind),
    FULL_HOUSE("Full House", 40, 4, ::hasFullHouse),
    FLUSH("Flush", 35, 4, ::hasFlush),
    STRAIGHT("Straight", 30, 4, ::hasStraight),
    THREE_OF_A_KIND("Three of a Kind", 30, 3, ::hasThreeOfAKind),
    TWO_PAIR("Two Pair", 20, 2, ::hasTwoPair),
    PAIR("Pair", 10, 2, ::hasPair),
    HIGH_CARD("High Card", 5, 1, ::hasHighCard)
}

// TODO:
//  Smeared Joker: "Hearts and Diamonds count as the same suit, Spades and Clubs count as the same suit" https://balatrogame.fandom.com/wiki/Smeared_Joker

fun hasHighCard(hand: List<PokerCard>): Boolean = hand.isNotEmpty()

fun hasPair(hand: List<PokerCard>): Boolean =
    rankCounts(hand).any { it >= 2 }

fun hasTwoPair(hand: List<PokerCard>): Boolean =
    rankCounts(hand).filter { it >= 2 }.size >= 2

fun hasThreeOfAKind(hand: List<PokerCard>): Boolean =
    rankCounts(hand).any { it >= 3 }

// Check if cards have consecutive ranks. Ace can be high or low.
fun hasStraight(hand: List<PokerCard>, fourFingers: Boolean = false, shortcut: Boolean = false): Boolean {
    val threshold = if (fourFingers) 4 else 5
    val values: List<Int> = hand.map(PokerCard::rank).map(Rank::value).distinct().sorted()

    if (longestConsecutiveSequence(values, shortcut) >= threshold)
        return true
    // Check Ace-low straight (Ace,2,3,4,5)
    val valuesLow: List<Int> = values.map { if (it == 14) 1 else it }.sorted()
    return longestConsecutiveSequence(valuesLow, shortcut) >= threshold
}

// Check if all cards have the same suit.
// TODO Smeared Joker
fun hasFlush(hand: List<PokerCard>, fourFingers: Boolean = false): Boolean {
    val threshold = if (fourFingers) 4 else 5
    return hand.groupBy(PokerCard::suit).any { it.value.size >= threshold }
}

fun hasFullHouse(hand: List<PokerCard>): Boolean {
    val values: List<Int> = rankCounts(hand).sortedDescending()
    // Must have 3+ of 1 card then 2+ of another
    return values.size >= 2 && values.first() >= 3 && values[1] >= 2
}

fun hasFourOfAKind(hand: List<PokerCard>): Boolean =
    rankCounts(hand).any(4::equals)

fun hasStraightFlush(hand: List<PokerCard>, fourFingers: Boolean = false, shortcut: Boolean = false): Boolean =
    hasStraight(hand, fourFingers, shortcut) && hasFlush(hand, fourFingers)

fun hasRoyalFlush(hand: List<PokerCard>): Boolean {
    if (!hasStraightFlush(hand)) return false
    val ranksInHand = hand.map(PokerCard::rank).toSet()
    return ranksInHand == setOf(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)
}

// Secret unlockable hands in Balatro

fun hasFiveOfAKind(hand: List<PokerCard>): Boolean =
    rankCounts(hand).any(5::equals)

fun hasFlushHouse(hand: List<PokerCard>, fourFingers: Boolean = false): Boolean =
    hasFullHouse(hand) && hasFlush(hand, fourFingers)

fun hasFlushFive(hand: List<PokerCard>, fourFingers: Boolean = false): Boolean {
    return hasFiveOfAKind(hand) && hasFlush(hand, fourFingers)
}