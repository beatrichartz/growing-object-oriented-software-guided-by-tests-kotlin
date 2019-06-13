package unit.auctionsniper

import auctionsniper.Auction
import auctionsniper.AuctionEventListener.PriceSource
import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import auctionsniper.SniperState
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test

class AuctionSniperTest {
    companion object {
        const val ITEM_ID = "item-id"
    }

    private val context = JUnit5Mockery()
    private val sniperListener = context.mock(SniperListener::class.java)
    private val auction = context.mock(Auction::class.java)
    private val sniper = AuctionSniper(auction, sniperListener, ITEM_ID)
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
            allowing(sniperListener).sniperBidding(
                    SniperState(ITEM_ID, 123, 168)
            )
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
        val bid = price + increment

        context.expect {
            oneOf(auction).bid(bid)
            atLeast(1).of(sniperListener).sniperBidding(
                    SniperState(ITEM_ID, price, bid)
            )
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