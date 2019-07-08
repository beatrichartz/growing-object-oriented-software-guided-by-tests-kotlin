package integration.auctionsniper.xmpp

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionHouse
import auctionsniper.XMPPAuctionHouse
import org.jivesoftware.smack.XMPPException
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import support.auctionsniper.ApplicationRunner
import support.auctionsniper.FakeAuctionServer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class XMPPAuctionHouseTest {
    private val auctionServer = FakeAuctionServer("item-54321")
    private lateinit var auctionHouse: AuctionHouse

    @BeforeEach
    @Throws(XMPPException::class)
    fun connectAuctionHouse() {
        auctionHouse = XMPPAuctionHouse.connect(
                FakeAuctionServer.XMPP_HOSTNAME,
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD)
    }

    @BeforeEach
    @Throws(XMPPException::class)
    fun startTheAuction() {
        auctionServer.startSellingItem()
    }

    @AfterEach
    fun disconnectAuctionHouse() {
        auctionHouse.disconnect()
    }

    @AfterEach
    fun stopAuction() {
        auctionServer.stop()
    }

    @Test
    @Throws(Exception::class)
    fun receivesEventsFromAuctionServerAfterJoining() {
        val auctionWasClosed = CountDownLatch(1)
        val auction = auctionHouse.auctionFor(auctionServer.itemId)
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed))
        auction.join()
        auctionServer.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID)
        auctionServer.announceClosed()

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS))
    }

    private fun auctionClosedListener(auctionWasClosed: CountDownLatch): AuctionEventListener {
        return object : AuctionEventListener {
            override fun auctionClosed() {
                auctionWasClosed.countDown()
            }

            override fun currentPrice(price: Int, increment: Int, priceSource: AuctionEventListener.PriceSource) {
                // not implemented
            }
        }
    }
}

