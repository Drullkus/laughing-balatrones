package us.drullk

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PokerHandTests {
    @Test
    fun highCardHand() {
        assertFalse(hasHighCard(listOf()), "High Card needs cards")

        // Every hand combination with a single card should qualify for High Card
        assertTrue(pokerDeckGenerator().all { card ->
            val hasHighCard = hasHighCard(listOf(card))
            assertTrue(hasHighCard, "High Card with $card")
            hasHighCard
        }, "Every single card for High Card")
    }

    @Test
    fun pairHand() {
        // There should exist no hand combinations of single-handed plays to qualify as a Pair
        assertFalse(pokerDeckGenerator().any { card ->
            val hasPair = hasPair(listOf(card))
            assertFalse(hasPair, "Pair with $card")
            hasPair
        }, "No singles for Pair hand")

        pokerDeckGenerator().forEach { pokerCard ->
            assertFalse(hasPair(listOf(
                pokerCard
            )), "Pair with only $pokerCard")

            pokerDeckGenerator().forEach { pokerCard2 ->
                assertFalse(hasPair(listOf(
                    pokerCard2
                )), "Pair with only $pokerCard2")

                val canPair = pokerCard.rank == pokerCard2.rank

                assertEquals(canPair, hasPair(listOf(
                    pokerCard, pokerCard2
                )), "Pair with $pokerCard and $pokerCard2 == $canPair")
            }
        }
    }

    @Test
    fun twoPairHand() {
        val hand = listOf(
            PokerCard(Suit.HEARTS, Rank.ACE),
            PokerCard(Suit.DIAMONDS, Rank.ACE),
            PokerCard(Suit.SPADES, Rank.TWO),
            PokerCard(Suit.CLUBS, Rank.TWO)
        )
        assertTrue(hasTwoPair(hand), "Two Pair hand with $hand")
    }

    @Test
    fun threeOfKindHand() {
        val hand = listOf(
            PokerCard(Suit.SPADES, Rank.TWO),
            PokerCard(Suit.CLUBS, Rank.TWO),
            PokerCard(Suit.DIAMONDS, Rank.TWO)
        )
        assertTrue(hasThreeOfAKind(hand), "Three-Of-A-Kind hand with $hand")
    }

    @Test
    fun straightHand() {
        val straight4 = listOf(
            PokerCard(Suit.SPADES, Rank.ACE),
            PokerCard(Suit.CLUBS, Rank.TWO),
            PokerCard(Suit.DIAMONDS, Rank.THREE),
            PokerCard(Suit.HEARTS, Rank.FOUR)
        )
        val straight5 = listOf(
            PokerCard(Suit.SPADES, Rank.ACE),
            PokerCard(Suit.CLUBS, Rank.TWO),
            PokerCard(Suit.DIAMONDS, Rank.THREE),
            PokerCard(Suit.HEARTS, Rank.FOUR),
            PokerCard(Suit.SPADES, Rank.FIVE)
        )
        val straight4Skipped = listOf(
            PokerCard(Suit.SPADES, Rank.ACE),
            PokerCard(Suit.CLUBS, Rank.TWO),
            // SKIP: 3
            PokerCard(Suit.DIAMONDS, Rank.FOUR),
            PokerCard(Suit.HEARTS, Rank.FIVE)
        )
        val straight5Skipped = listOf(
            PokerCard(Suit.SPADES, Rank.ACE),
            PokerCard(Suit.CLUBS, Rank.TWO),
            PokerCard(Suit.DIAMONDS, Rank.THREE),
            // SKIP: 4
            PokerCard(Suit.HEARTS, Rank.FIVE),
            PokerCard(Suit.SPADES, Rank.SIX)
        )
        assertFalse(hasStraight(straight4, false, false), "1. Straight hand with $straight4")
        assertTrue(hasStraight(straight5, false, false), "2. Straight hand with $straight5")
        assertFalse(hasStraight(straight4Skipped, false, false), "3. Straight hand with $straight4Skipped")
        assertFalse(hasStraight(straight5Skipped, false, false), "4. Straight hand with $straight5Skipped")

        assertTrue(hasStraight(straight4, true, false), "5. Four-fingered Straight hand with $straight4")
        assertTrue(hasStraight(straight5, true, false), "6. Four-fingered Straight hand with $straight5")
        assertFalse(hasStraight(straight4Skipped, true, false), "7. Four-fingered Straight hand with $straight4Skipped")
        assertFalse(hasStraight(straight5Skipped, true, false), "8. Four-fingered Straight hand with $straight5Skipped")

        assertFalse(hasStraight(straight4, false, true), "9. Shortcutted Straight hand with $straight4")
        assertTrue(hasStraight(straight5, false, true), "10. Shortcutted Straight hand with $straight5")
        assertFalse(hasStraight(straight4Skipped, false, true), "11. Shortcutted Straight hand with $straight4Skipped")
        assertTrue(hasStraight(straight5Skipped, false, true), "12. Shortcutted Straight hand with $straight5Skipped")

        assertTrue(hasStraight(straight4, true, true), "13. Four-fingered Shortcutted Straight hand with $straight4")
        assertTrue(hasStraight(straight5, true, true), "14. Four-fingered Shortcutted Straight hand with $straight5")
        assertTrue(hasStraight(straight4Skipped, true, true), "15. Four-fingered Shortcutted Straight hand with $straight4Skipped")
        assertTrue(hasStraight(straight5Skipped, true, true), "16. Four-fingered Shortcutted Straight hand with $straight5Skipped")
    }

    @Test
    fun fullHouseHand() {
        val hand = listOf(
            PokerCard(Suit.HEARTS, Rank.ACE),
            PokerCard(Suit.DIAMONDS, Rank.ACE),
            PokerCard(Suit.SPADES, Rank.TWO),
            PokerCard(Suit.CLUBS, Rank.TWO),
            PokerCard(Suit.DIAMONDS, Rank.TWO)
        )
        assertTrue(hasFullHouse(hand), "Full House hand with $hand")
    }
}
