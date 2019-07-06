package auctionsniper

interface Auction {
    fun bid(amount: Int)
    fun join()
    fun addAuctionEventListener(auctionEventListener: AuctionEventListener)
}
