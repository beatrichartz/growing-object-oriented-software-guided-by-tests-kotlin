package auctionsniper

class AuctionSniper(private val sniperListener: SniperListener) : AuctionEventListener {
    override fun auctionClosed() {
        sniperListener.sniperLost()
    }

    override fun currentPrice(price: Int, increment: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
