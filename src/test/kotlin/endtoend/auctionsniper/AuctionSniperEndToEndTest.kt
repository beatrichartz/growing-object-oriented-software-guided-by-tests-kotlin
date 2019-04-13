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
        auction.hasReceivedJoinRequestFromSniper()
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