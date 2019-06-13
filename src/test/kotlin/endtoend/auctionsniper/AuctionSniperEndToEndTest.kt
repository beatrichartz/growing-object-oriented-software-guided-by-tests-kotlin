package endtoend.auctionsniper

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuctionSniperEndToEndTest {
    private lateinit var auction: FakeAuctionServer
    private lateinit var application: ApplicationRunner

    @BeforeEach
    internal fun setupAuction() {
        auction = FakeAuctionServer("item-54321")
    }

    @BeforeEach
    internal fun setupApplication() {
        application = ApplicationRunner()
    }

    @Test
    internal fun sniperWinsAnAuctionByBiddingHigher() {
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(1000, 1098)

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID)
        application.hasShownSniperisWinning(1098)

        auction.announceClosed()
        application.showsSniperHasWonAuction(1098)
    }

    @Test
    internal fun sniperJoinsAuctionUntilAuctionCloses() {
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(1000, 1098)

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction()
    }

    @AfterEach
    internal fun stopAuction() {
        auction.stop()
    }

    @AfterEach
    internal fun stopApplication() {
        application.stop()
    }
}