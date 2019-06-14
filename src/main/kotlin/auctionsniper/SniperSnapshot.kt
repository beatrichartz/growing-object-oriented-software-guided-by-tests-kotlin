package auctionsniper

enum class SniperState {
    JOINING, BIDDING, WINNING, LOST, WON
}

class SniperSnapshot(internal val itemId: String,
                     internal val lastPrice: Int,
                     internal val lastBid: Int,
                     internal val state: SniperState) {

    companion object {
        fun joining(itemId: String): SniperSnapshot {
            return SniperSnapshot(itemId, 0, 0, SniperState.JOINING)
        }
    }

    fun winning(newLastPrice: Int): SniperSnapshot {
        return SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING)
    }

    fun bidding(newLastPrice: Int, newLastBid: Int): SniperSnapshot {
        return SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SniperSnapshot

        if (itemId != other.itemId) return false
        if (lastPrice != other.lastPrice) return false
        if (lastBid != other.lastBid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + lastPrice
        result = 31 * result + lastBid
        return result
    }

    override fun toString(): String {
        return "SniperSnapshot(itemId='$itemId', lastPrice=$lastPrice, lastBid=$lastBid)"
    }
}
