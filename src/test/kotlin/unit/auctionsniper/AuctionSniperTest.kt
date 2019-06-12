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
    private val sniperState = context.states("sniper")

    @Test
    internal fun reportsLostWhenAuctionClosesImmediately() {
        context.expect {
            oneOf(sniperListener).sniperLost()
        }.whenRunning {
            sniper.auctionClosed()
        }
    }

    @Test
    internal fun reportLostIfAuctionClosesWhenBidding() {
        context.expect {
            ignoring(auction)
            allowing(sniperListener).sniperBidding()
            then(sniperState.`is`("bidding"))

            atLeast(1).of(sniperListener).sniperLost()
            `when`(sniperState.`is`("bidding"))
        }.whenRunning {
            sniper.currentPrice(123, 45, PriceSource.FromOtherBidder)
            sniper.auctionClosed()
        }
    }

    @Test
    internal fun reportWonIfAuctionClosesWhenWinning() {
        context.expect {
            ignoring(auction)
            allowing(sniperListener).sniperWinning()
            then(sniperState.`is`("winning"))

            atLeast(1).of(sniperListener).sniperWon()
            `when`(sniperState.`is`("winning"))
        }.whenRunning {
            sniper.currentPrice(123, 45, PriceSource.FromSniper)
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

    @Test
    internal fun reportIsWinningWhenCurrentPriceComesFromSniper() {
        context.expect {
            atLeast(1).of(sniperListener).sniperWinning()
        }.whenRunning {
            sniper.currentPrice(123, 45, PriceSource.FromSniper)
        }
    }
}