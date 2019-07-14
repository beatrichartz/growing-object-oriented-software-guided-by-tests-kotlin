package auctionsniper

class Item(val identifier: String, val stopPrice: Long) {
    fun allows(bid: Int): Boolean {
        return bid <= stopPrice
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (identifier != other.identifier) return false
        if (stopPrice != other.stopPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + stopPrice.hashCode()
        return result
    }

    override fun toString(): String {
        return "Item(identifier='$identifier', stopPrice=$stopPrice)"
    }
}