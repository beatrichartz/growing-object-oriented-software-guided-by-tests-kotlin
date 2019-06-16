package endtoend.auctionsniper

import auctionsniper.Main
import auctionsniper.MainWindow
import auctionsniper.SniperState.*
import auctionsniper.SnipersTableModel.Companion.textFor
import endtoend.auctionsniper.FakeAuctionServer.Companion.AUCTION_RESOURCE
import endtoend.auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME

class ApplicationRunner {
    companion object {
        const val SNIPER_ID = "sniper"
        const val SNIPER_PASSWORD = "sniper"
        const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"
    }

    private lateinit var driver: AuctionSniperDriver

    fun startBiddingIn(vararg auctions: FakeAuctionServer) {
        val thread = Thread {
            try {
                Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD,
                        *auctions.map { it.itemId }.toTypedArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.isDaemon = true
        thread.start()

        driver = AuctionSniperDriver(1000)
        driver.hasTitle(MainWindow.APPLICATION_TITLE)
        driver.hasColumnTitles()
    }

    fun hasShownSniperIsBidding(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) {
        driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, textFor(BIDDING))
    }

    fun hasShownSniperIsWinning(auction: FakeAuctionServer, winningBid: Int) {
        driver.showsSniperStatus(auction.itemId, winningBid, winningBid, textFor(WINNING))
    }

    fun showsSniperHasLostAuction(auction: FakeAuctionServer) {
        driver.showsSniperStatus(auction.itemId, textFor(LOST))
    }

    fun showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) {
        driver.showsSniperStatus(auction.itemId, lastPrice, lastPrice, textFor(WON))
    }

    fun stop() {
        driver.dispose()
    }

}
