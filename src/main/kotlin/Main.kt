package us.drullk

/**
 * For calculating hand probabilities in Balatro, a roguelike deck-building game.
 */
fun main() {
    val deckChances = vectorizeDeckChances(pokerDeckGenerator().toList())

    for (hand in PokerHand.entries) {
        println("Percentile of ${hand.displayName}: ${"%.6f".format(deckChances[hand])}%")
    }

    // TODO Million dollar question - how will probabilities change when I add a card to the deck?
    //  Find vector delta between hand-chance vectors generated from pre-addition and post-addition decks
    //  Out of a booster pack with 3 random poker cards, choose card that maximizes chance for ?

    // TODO Balatro math
    //  In the first round, it is possible to reach 300 chips with combinations of poker hands and specific cards.
    //  Find all hands such that round 1 can be won with a single poker hand
}
