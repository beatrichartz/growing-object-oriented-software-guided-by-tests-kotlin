package endtoend.auctionsniper

import org.junit.After
import org.junit.jupiter.api.Test

class AuctionSniperEndToEndTest {
    companion object {
        val auction = FakeAuctionServer("item-54321")
        val application = ApplicationRunner()
    }

    @Test
    internal fun sniperJoinsAuctionUntilAuctionCloses() {
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding()

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction()
    }

    @After
    internal fun stopAuction() {
        auction.stop()
    }

    @After
    internal fun stopApplication() {
        application.stop()
    }
}