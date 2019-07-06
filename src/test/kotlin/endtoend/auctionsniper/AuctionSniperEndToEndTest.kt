package endtoend.auctionsniper

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import support.auctionsniper.ApplicationRunner
import support.auctionsniper.FakeAuctionServer

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
    internal fun sniperBidsForMultipleItems() {
        val auction2 = FakeAuctionServer("item-65432")

        auction.startSellingItem()
        auction2.startSellingItem()

        application.startBiddingIn(auction, auction2)

        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)
        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction2.reportPrice(500, 21, "other bidder")
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID)
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID)

        application.hasShownSniperIsWinning(auction, 1098)
        application.hasShownSniperIsWinning(auction2, 521)

        auction.announceClosed()
        auction2.announceClosed()

        application.showsSniperHasWonAuction(auction, 1098)
        application.showsSniperHasWonAuction(auction2, 521)
    }

    @Test
    internal fun sniperWinsAnAuctionByBiddingHigher() {
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction, 1098)

        auction.announceClosed()
        application.showsSniperHasWonAuction(auction, 1098)
    }

    @Test
    internal fun sniperJoinsAuctionUntilAuctionCloses() {
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction(auction)
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