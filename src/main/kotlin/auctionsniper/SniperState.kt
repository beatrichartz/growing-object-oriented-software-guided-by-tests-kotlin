package auctionsniper

class SniperState(internal val itemId: String,
                  internal val lastPrice: Int,
                  internal val lastBid: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SniperState

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
        return "SniperState(itemId='$itemId', lastPrice=$lastPrice, lastBid=$lastBid)"
    }
}
