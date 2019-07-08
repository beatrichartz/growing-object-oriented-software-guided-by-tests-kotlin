package unit.auctionsniper

import auctionsniper.Auction
import auctionsniper.AuctionEventListener

class NullAuction: Auction {
    override fun bid(amount: Int) {
    }

    override fun join() {
    }

    override fun addAuctionEventListener(auctionEventListener: AuctionEventListener) {
    }
}

