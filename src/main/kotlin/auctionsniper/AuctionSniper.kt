package auctionsniper

import auctionsniper.AuctionEventListener.PriceSource
import eventhandling.Announcer

class AuctionSniper(itemId: String,
                    private val auction: Auction) : AuctionEventListener {
    var snapshot = SniperSnapshot.joining(itemId)
    private val listeners = Announcer.toListenerType(SniperListener::class.java)

    override fun auctionClosed() {
        snapshot = snapshot.closed()
        notifyChange()
    }

    override fun currentPrice(price: Int, increment: Int, priceSource: PriceSource) {
        snapshot = when (priceSource) {
            PriceSource.FromSniper ->
                snapshot.winning(price)
            PriceSource.FromOtherBidder -> {
                val bid = price + increment
                auction.bid(bid)
                snapshot.bidding(price, bid)
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
