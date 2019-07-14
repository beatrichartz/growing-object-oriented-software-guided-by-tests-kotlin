package unit.auctionsniper

import auctionsniper.*
import auctionsniper.AuctionEventListener.PriceSource
import auctionsniper.SniperState.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.FeatureMatcher
import org.hamcrest.Matcher
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class AuctionSniperTest {
    companion object {
        val ITEM = Item("item-id", 1200)
    }

    private val context = JUnit5Mockery()
    private val sniperListener = context.mock(SniperListener::class.java)
    private val auction = context.mock(Auction::class.java)
    private val sniper = AuctionSniper(ITEM, auction)
    private val sniperState = context.states("sniper")

    @BeforeEach
    internal fun addSniperListener() {
        sniper.addSniperListener(sniperListener)
    }

    @Test
    internal fun reportsLostWhenAuctionClosesImmediately() {
        context.expect {
            oneOf(sniperListener).sniperStateChanged(
                    with(aSniperThatHas(LOST))
            )
        }.whenRunning {
            sniper.auctionClosed()
        }
    }

    @Test
    internal fun reportLostIfAuctionClosesWhenBidding() {
        context.expect {
            ignoring(auction)
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING))
            )
            then(sniperState.`is`("bidding"))

            atLeast(1).of(sniperListener).sniperStateChanged(
                    with(aSniperThatHas(LOST))
            )
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
                    with(aSniperThatIs(WINNING))
            )
            then(sniperState.`is`("winning"))

            atLeast(1).of(sniperListener).sniperStateChanged(
                    with(aSniperThatHas(WON))
            )
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
                    SniperSnapshot(ITEM, price, bid, BIDDING)
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
                    with(aSniperThatIs(BIDDING))
            )
            then(sniperState.`is`("bidding"))

            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM, 135, 135, WINNING)
            )
            `when`(sniperState.`is`("bidding"))
        }.whenRunning {
            sniper.currentPrice(123, 12, PriceSource.FromOtherBidder)
            sniper.currentPrice(135, 45, PriceSource.FromSniper)
        }
    }

    @Test
    fun doesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice() {
        context.expect {
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING)))
            then(sniperState.`is`("bidding"))

            val bid = 123 + 45
            allowing(auction).bid(bid)
            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM, 2345, bid, LOSING))
            `when`(sniperState.`is`("bidding"))
        }.whenRunning {
            sniper.currentPrice(123, 45, PriceSource.FromOtherBidder)
            sniper.currentPrice(2345, 25, PriceSource.FromOtherBidder)
        }
    }

    @Test
    fun doesNotBidAndReportsLosingIfFirstPriceIsAboveStopPrice() {
        val price = 1500
        val increment = 25

        context.expect {
            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM, price, 0, LOSING))
        }.whenRunning {
            sniper.currentPrice(price, increment, PriceSource.FromOtherBidder)
        }
    }

    @Test
    fun continuesToBeLosingOnceStopPriceHasBeenReached() {
        val states = context.sequence("sniper states")
        val price1 = 1233
        val price2 = 1258

        context.expect {
            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM, price1, 0, LOSING))
            inSequence(states)
            atLeast(1).of(sniperListener).sniperStateChanged(
                    SniperSnapshot(ITEM, price2, 0, LOSING))
            inSequence(states)
        }.whenRunning {
            sniper.currentPrice(price1, 25, PriceSource.FromOtherBidder)
            sniper.currentPrice(price2, 25, PriceSource.FromOtherBidder)
        }
    }

    @Test
    fun doesNotBidAndReportsLosingIfPriceAfterWinningIsAboveStopPrice() {
        val price = 1233
        val increment = 25

        context.expect {
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING)))
            then(sniperState.`is`("bidding"))
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(WINNING)))
            then(sniperState.`is`("winning"))

            val bid = 123 + 45
            allowing(auction).bid(bid)

            atLeast(1).of(sniperListener).sniperStateChanged(SniperSnapshot(ITEM, price, bid, LOSING))
            `when`(sniperState.`is`("winning"))
        }.whenRunning {
            sniper.currentPrice(123, 45, PriceSource.FromOtherBidder)
            sniper.currentPrice(168, 45, PriceSource.FromSniper)
            sniper.currentPrice(price, increment, PriceSource.FromOtherBidder)
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

    private fun aSniperThatHas(state: SniperState): Matcher<SniperSnapshot> {
        return aSniperThatIs(state)
    }
}


