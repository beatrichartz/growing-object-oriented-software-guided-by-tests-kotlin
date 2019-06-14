package auctionsniper

import auctionsniper.AuctionEventListener.PriceSource

class AuctionSniper(private val auction: Auction,
                    private val sniperListener: SniperListener,
                    itemId: String) : AuctionEventListener {
    private var snapshot = SniperSnapshot.joining(itemId)

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

    private fun notifyChange() {
        sniperListener.sniperStateChanged(snapshot)
    }

}
