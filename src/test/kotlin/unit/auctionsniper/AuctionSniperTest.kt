package unit.auctionsniper

import auctionsniper.Auction
import auctionsniper.AuctionEventListener.PriceSource
import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test

class AuctionSniperTest {
    private val context = JUnit5Mockery()
    private val sniperListener = context.mock(SniperListener::class.java)
    private val auction = context.mock(Auction::class.java)
    private val sniper = AuctionSniper(auction, sniperListener)

    @Test
    internal fun reportsLostWhenAuctionCloses() {
        context.expect {
            oneOf(sniperListener).sniperLost()
        }.whenRunning {
            sniper.auctionClosed()
        }
    }

    @Test
    internal fun bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        val price = 1001
        val increment = 25

        context.expect {
            oneOf(auction).bid(price + increment)
            atLeast(1).of(sniperListener).sniperBidding()
        }.whenRunning {
            sniper.currentPrice(price, increment, PriceSource.FromOtherBidder)
        }
    }
}