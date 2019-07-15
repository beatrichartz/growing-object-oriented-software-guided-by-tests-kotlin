package endtoend.auctionsniper

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import support.auctionsniper.FakeAuctionServer
import support.auctionsniper.ApplicationRunner

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
    internal fun sniperLosesAnAuctionWhenThePriceIsTooHigh() {
        auction.startSellingItem()

        application.startBiddingWithStopPrice(auction, 1100)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)
        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1197, 10, "third party")
        application.hasShownSniperIsLosing(auction, 1197, 1098)

        auction.reportPrice(1207, 10, "fourth party")
        application.hasShownSniperIsLosing(auction, 1207, 1098)
        auction.announceClosed()
        application.showsSniperHasLostAuction(auction, 1207, 1098)
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

    @Test
    fun sniperReportsInvalidAuctionMessageAndStopsRespondingToEvents() {
        val brokenMessage = "a broken message"

        auction.startSellingItem()
        val auction2 = FakeAuctionServer("item-65432")
        auction2.startSellingItem()

        application.startBiddingIn(auction, auction2)
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(500, 20, "other bidder")
        auction.hasReceivedBid(520, ApplicationRunner.SNIPER_XMPP_ID)

        auction.sendInvalidMessageContaining(brokenMessage)
        application.showsSniperHasFailed(auction)

        auction.reportPrice(520, 21, "other bidder")

        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)
        auction2.reportPrice(600, 6, "other bidder")
        application.hasShownSniperIsBidding(auction2, 600, 606)

        application.reportsInvalidMessage(auction, brokenMessage)
        application.showsSniperHasFailed(auction)
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