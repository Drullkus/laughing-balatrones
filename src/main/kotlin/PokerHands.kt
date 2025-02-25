package us.drullk

/**
 * Poker hands valid in Balatro: https://balatrogame.fandom.com/wiki/Poker_Hands
 */
fun isHighCard(hand: List<PokerCard>): Boolean =
    !(isPair(hand) || isTwoPair(hand) || isThreeOfAKind(hand) ||
            isStraight(hand) || isFlush(hand) || isFullHouse(hand) ||
            isFourOfAKind(hand) || isStraightFlush(hand) || isRoyalFlush(hand))

fun isPair(hand: List<PokerCard>): Boolean {
    val counts = rankCounts(hand).values
    return counts.count { it == 2 } == 1 && counts.count { it == 1 } == 3
}

fun isTwoPair(hand: List<PokerCard>): Boolean =
    rankCounts(hand).filter { it.value == 2 }.size == 2


fun isThreeOfAKind(hand: List<PokerCard>): Boolean {
    val counts = rankCounts(hand).values
    return counts.contains(3) && counts.sorted() != listOf(2, 3)
}

// Check if cards have consecutive ranks. Ace can be high or low.
fun isStraight(hand: List<PokerCard>): Boolean {
    val values = hand.map { it.rank.value }.sorted()
    val normalStraight = (0 until values.size - 1).all { i -> values[i] + 1 == values[i + 1] }
    if (normalStraight) return true
    // Check Ace-low straight (Ace,2,3,4,5)
    val valuesLow = values.map { if (it == 14) 1 else it }.sorted()
    return (0 until valuesLow.size - 1).all { i -> valuesLow[i] + 1 == valuesLow[i + 1] }
}

// Check if all cards have the same suit.
fun isFlush(hand: List<PokerCard>): Boolean =
    hand.map { it.suit }.distinct().size == 1

fun isFullHouse(hand: List<PokerCard>): Boolean =
    rankCounts(hand).values.sorted() == listOf(2, 3)

fun isFourOfAKind(hand: List<PokerCard>): Boolean =
    rankCounts(hand).values.any { it == 4 }

fun isStraightFlush(hand: List<PokerCard>): Boolean =
    isStraight(hand) && isFlush(hand)

fun isRoyalFlush(hand: List<PokerCard>): Boolean {
    if (!isStraightFlush(hand)) return false
    val ranksInHand = hand.map { it.rank }.toSet()
    return ranksInHand == setOf(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)
}

// Secret unlockable hands in Balatro

fun isFiveOfAKind(hand: List<PokerCard>): Boolean =
    rankCounts(hand).values.any { it == 5 }

fun isFlushHouse(hand: List<PokerCard>): Boolean =
    isFullHouse(hand) && isFlush(hand)

fun isFlushFive(hand: List<PokerCard>): Boolean {
    if (hand.isEmpty()) return false
    val first = hand[0]
    return hand.all { it.suit == first.suit && it.rank == first.rank }
}