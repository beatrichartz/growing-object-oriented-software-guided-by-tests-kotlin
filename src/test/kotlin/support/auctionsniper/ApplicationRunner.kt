package support.auctionsniper

import auctionsniper.Main
import auctionsniper.SniperState
import auctionsniper.SniperState.*
import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SnipersTableModel.Companion.textFor
import support.auctionsniper.FakeAuctionServer.Companion.AUCTION_RESOURCE
import support.auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME

class ApplicationRunner {
    companion object {
        const val SNIPER_ID = "sniper"
        const val SNIPER_PASSWORD = "sniper"
        const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"
    }

    private lateinit var driver: AuctionSniperDriver

    fun startBiddingIn(vararg auctions: FakeAuctionServer) {
        startSniper()

        for (auction in auctions) {
            val itemId = auction.itemId
            driver.startBiddingFor(itemId)
            driver.showsSniperStatus(itemId, textFor(JOINING))
        }
    }

    fun startBiddingWithStopPrice(auction: FakeAuctionServer, stopPrice: Int) {
        startSniper()

        val itemId = auction.itemId
        driver.startBiddingFor(itemId, stopPrice)
        driver.showsSniperStatus(itemId, 0, 0, textFor(JOINING))
    }

    private fun startSniper() {
        val thread = Thread {
            try {
                Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD)
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

    fun showsSniperHasFailed(auction: FakeAuctionServer) {
        driver.showsSniperStatus(auction.itemId, 0, 0, SnipersTableModel.textFor(SniperState.FAILED))
    }

    fun reportsInvalidMessage(auction: FakeAuctionServer, brokenMessage: String) {
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

    fun showsSniperHasLostAuction(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) {
        driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, SnipersTableModel.textFor(LOST))
    }

    fun hasShownSniperIsLosing(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) {
        driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, SnipersTableModel.textFor(LOSING))
    }

    fun showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) {
        driver.showsSniperStatus(auction.itemId, lastPrice, lastPrice, textFor(WON))
    }

    fun stop() {
        driver.dispose()
    }

}
