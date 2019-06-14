package unit.auctionsniper

import auctionsniper.*
import auctionsniper.AuctionEventListener.PriceSource
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.FeatureMatcher
import org.hamcrest.Matcher
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
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.BIDDING))
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
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.WINNING))
            )
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
            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING)
            )
        }.whenRunning {
            sniper.currentPrice(price, increment, PriceSource.FromOtherBidder)
        }
    }

    @Test
    internal fun reportIsWinningWhenCurrentPriceComesFromSniper() {
        context.expect {
            ignoring(auction)
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.BIDDING))
            )
            then(sniperState.`is`("bidding"))

            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING)
            )
            `when`(sniperState.`is`("bidding"))
        }.whenRunning {
            sniper.currentPrice(123, 12, PriceSource.FromOtherBidder)
            sniper.currentPrice(135, 45, PriceSource.FromSniper)
        }
    }

    private fun aSniperThatIs(state: SniperState): Matcher<SniperSnapshot> {
        return object : FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was") {
            override fun featureValueOf(actual: SniperSnapshot): SniperState {
                return actual.state
            }
        }
    }

}


