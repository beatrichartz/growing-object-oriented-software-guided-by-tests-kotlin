package auctionsniper

import java.util.*

interface AuctionEventListener: EventListener {
    enum class PriceSource {
        FromSniper, FromOtherBidder
    }

    fun auctionClosed()
    fun currentPrice(price: Int, increment: Int, priceSource: PriceSource)
    fun auctionFailed()
}