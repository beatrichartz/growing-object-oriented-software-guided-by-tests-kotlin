package auctionsniper

enum class Column(val title: String) {
    ITEM_IDENTIFIER("") {
        override fun valueIn(snapshot: SniperSnapshot): String {
            return snapshot.itemId
        }
    },
    LAST_PRICE("") {
        override fun valueIn(snapshot: SniperSnapshot): Int {
            return snapshot.lastPrice
        }
    },
    LAST_BID("") {
        override fun valueIn(snapshot: SniperSnapshot): Int {
            return snapshot.lastBid
        }
    },
    SNIPER_STATE("") {
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