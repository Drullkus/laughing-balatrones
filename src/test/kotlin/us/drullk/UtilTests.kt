package us.drullk

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UtilTests {
    @Test
    fun testCounts() {
        assertTrue(rankCounts(listOf()).isEmpty(), "No ranks")

        assertContains(rankCounts(listOf(
            PokerCard(Suit.HEARTS, Rank.ACE)
        )), 1, "One unique rank from 1 card")

        assertContains(rankCounts(listOf(
            PokerCard(Suit.HEARTS, Rank.ACE),
            PokerCard(Suit.DIAMONDS, Rank.ACE),
            PokerCard(Suit.CLUBS, Rank.ACE)
        )).distinct(), 3, "One unique rank from 3 cards")

        assertEquals(rankCounts(listOf(
            PokerCard(Suit.HEARTS, Rank.ACE),
            PokerCard(Suit.HEARTS, Rank.TWO),
            PokerCard(Suit.HEARTS, Rank.THREE)
        )).count(), 3, "Three ranks from 3 cards with differing ranks")

        val twoPair = rankCounts(
            listOf(
                PokerCard(Suit.HEARTS, Rank.ACE),
                PokerCard(Suit.SPADES, Rank.TWO),
                PokerCard(Suit.CLUBS, Rank.ACE),
                PokerCard(Suit.DIAMONDS, Rank.TWO)
            )
        )
        assertEquals(twoPair.count(), 2, "Two different ranks")
        assertContains(twoPair.distinct(), 2, "Two unique ranks")
        assertEquals(twoPair.distinct().count(), 1, "Both unique ranks have same count")
    }
}
