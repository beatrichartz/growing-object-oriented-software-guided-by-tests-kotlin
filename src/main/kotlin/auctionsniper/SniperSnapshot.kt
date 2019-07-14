package auctionsniper

enum class SniperState {
    JOINING {
        override fun whenAuctionClosed(): SniperState {
            return LOST
        }
    },
    BIDDING {
        override fun whenAuctionClosed(): SniperState {
            return LOST
        }
    },
    WINNING {
        override fun whenAuctionClosed(): SniperState {
            return WON
        }
    },
    LOSING {
        override fun whenAuctionClosed(): SniperState {
            return LOST
        }
    },
    LOST, WON;

    open fun whenAuctionClosed(): SniperState {
        throw IllegalStateException("Auction is already closed")
    }
}

class SniperSnapshot(internal val item: Item,
                     internal val lastPrice: Int,
                     internal val lastBid: Int,
                     internal val state: SniperState) {

    companion object {
        fun joining(item: Item): SniperSnapshot {
            return SniperSnapshot(item, 0, 0, SniperState.JOINING)
        }
    }

    fun winning(newLastPrice: Int): SniperSnapshot {
        return SniperSnapshot(item, newLastPrice, lastBid, SniperState.WINNING)
    }

    fun losing(price: Int): SniperSnapshot {
        return SniperSnapshot(item, price, lastBid, SniperState.LOSING)
    }

    fun bidding(newLastPrice: Int, newLastBid: Int): SniperSnapshot {
        return SniperSnapshot(item, newLastPrice, newLastBid, SniperState.BIDDING)
    }

    fun closed(): SniperSnapshot {
        return SniperSnapshot(item, lastPrice, lastBid, state.whenAuctionClosed())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SniperSnapshot

        if (item != other.item) return false
        if (lastPrice != other.lastPrice) return false
        if (lastBid != other.lastBid) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = item.hashCode()
        result = 31 * result + lastPrice
        result = 31 * result + lastBid
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString(): String {
        return "SniperSnapshot(item=$item, lastPrice=$lastPrice, lastBid=$lastBid, state=$state)"
    }
}
