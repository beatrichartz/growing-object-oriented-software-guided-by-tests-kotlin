package auctionsniper

interface AuctionEventListener {
    fun auctionClosed()
    fun currentPrice(price: Int, increment: Int)
}