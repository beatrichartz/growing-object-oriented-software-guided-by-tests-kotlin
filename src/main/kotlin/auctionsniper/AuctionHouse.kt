package auctionsniper

interface AuctionHouse {
    fun auctionFor(itemId: String): Auction
    fun disconnect()
}