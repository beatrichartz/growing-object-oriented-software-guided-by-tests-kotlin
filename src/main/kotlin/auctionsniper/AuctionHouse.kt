package auctionsniper

interface AuctionHouse {
    fun auctionFor(item: Item): Auction
    fun disconnect()
}