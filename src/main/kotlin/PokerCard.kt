package us.drullk

data class PokerCard(val suit: Suit, val rank: Rank) {
    override fun toString(): String = "${rank.name.capitalize()} of ${suit.name.capitalize()}"
}