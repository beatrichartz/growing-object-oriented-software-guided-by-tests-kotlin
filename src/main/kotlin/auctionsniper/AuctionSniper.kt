package auctionsniper

import auctionsniper.AuctionEventListener.PriceSource
import eventhandling.Announcer

class AuctionSniper(private val item: Item,
                    private val auction: Auction) : AuctionEventListener {
    var snapshot = SniperSnapshot.joining(item)
    private val listeners = Announcer.toListenerType(SniperListener::class.java)

    override fun auctionClosed() {
        snapshot = snapshot.closed()
        notifyChange()
    }

    override fun auctionFailed() {
    }

    override fun currentPrice(price: Int, increment: Int, priceSource: PriceSource) {
        snapshot = when (priceSource) {
            PriceSource.FromSniper ->
                snapshot.winning(price)
            PriceSource.FromOtherBidder -> {
                val bid = price + increment
                if (item.allows(bid)) {
                    auction.bid(bid)
                    snapshot.bidding(price, bid)
                } else {
                    snapshot.losing(price)
                }
            }
        }

        notifyChange()
    }

    fun addSniperListener(listener: SniperListener) {
        listeners.addListener(listener)
    }

    private fun notifyChange() {
        listeners.announce().sniperStateChanged(snapshot)
    }
}
