package auctionsniper

class SniperState(private val itemId: String,
                  private val price: Int,
                  private val bid: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SniperState

        if (itemId != other.itemId) return false
        if (price != other.price) return false
        if (bid != other.bid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + price
        result = 31 * result + bid
        return result
    }

    override fun toString(): String {
        return "SniperState(itemId='$itemId', price=$price, bid=$bid)"
    }
}
