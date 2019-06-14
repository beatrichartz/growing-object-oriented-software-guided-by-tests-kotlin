package endtoend.auctionsniper

import auctionsniper.Main
import endtoend.auctionsniper.FakeAuctionServer.Companion.AUCTION_RESOURCE
import endtoend.auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME

class ApplicationRunner {
    companion object {
        const val SNIPER_ID = "sniper"
        const val SNIPER_PASSWORD = "sniper"
        const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"
    }

    private lateinit var itemId: String
    private lateinit var driver: AuctionSniperDriver

    fun startBiddingIn(auction: FakeAuctionServer) {
        itemId = auction.itemId
        val thread = Thread {
            try {
                Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.isDaemon = true
        thread.start()

        driver = AuctionSniperDriver(1000)
        driver.showsSniperStatus("Joining")
    }

    fun hasShownSniperIsBidding(lastPrice: Int, lastBid: Int) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, "Bidding")
    }

    fun hasShownSniperisWinning(winningBid: Int) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, "Winning")
    }

    fun showsSniperHasLostAuction() {
        driver.showsSniperStatus("Lost")
    }

    fun showsSniperHasWonAuction(lastPrice: Int) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, "Won")
    }

    fun stop() {
        driver.dispose()
    }

}
