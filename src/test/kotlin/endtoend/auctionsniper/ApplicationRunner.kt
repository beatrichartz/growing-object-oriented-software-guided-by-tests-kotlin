package endtoend.auctionsniper

import auctionsniper.Main
import auctionsniper.Main.Companion.STATUS_JOINING
import auctionsniper.Main.Companion.STATUS_LOST
import endtoend.auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME

class ApplicationRunner {
    companion object {
        const val SNIPER_ID = "sniper"
        const val SNIPER_PASSWORD = "sniper"
    }

    private lateinit var driver: AuctionSniperDriver

    fun startBiddingIn(auction: FakeAuctionServer) {
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
        driver.showsSniperStatus(STATUS_JOINING)
    }

    fun showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST)
    }

    fun stop() {
        driver.dispose()
    }
}
