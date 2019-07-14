package auctionsniper.ui

import auctionsniper.SniperSnapshot

enum class Column(val title: String) {
    ITEM_IDENTIFIER("Item") {
        override fun valueIn(snapshot: SniperSnapshot): String {
            return snapshot.item.identifier
        }
    },
    LAST_PRICE("Last Price") {
        override fun valueIn(snapshot: SniperSnapshot): Int {
            return snapshot.lastPrice
        }
    },
    LAST_BID("Last Bid") {
        override fun valueIn(snapshot: SniperSnapshot): Int {
            return snapshot.lastBid
        }
    },
    SNIPER_STATE("State") {
        override fun valueIn(snapshot: SniperSnapshot): String {
            return SnipersTableModel.textFor(snapshot.state)
        }
    };

    abstract fun valueIn(snapshot: SniperSnapshot): Any

    companion object {
        fun at(index: Int): Column {
            return values()[index]
        }
    }
}