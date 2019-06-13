package auctionsniper

import auctionsniper.AuctionEventListener.PriceSource

class AuctionSniper(private val auction: Auction,
                    private val sniperListener: SniperListener,
                    private val itemId: String) : AuctionEventListener {
    private var isWinning = false

    override fun auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon()
        } else {
            sniperListener.sniperLost()
        }
    }

    override fun currentPrice(price: Int, increment: Int, priceSource: PriceSource) {
        isWinning = priceSource == PriceSource.FromSniper

        if (isWinning) {
            sniperListener.sniperWinning()
        } else {
            val bid = price + increment
            auction.bid(bid)
            sniperListener.sniperBidding(
                    SniperState(itemId, price, bid)
            )
        }
    }

}
