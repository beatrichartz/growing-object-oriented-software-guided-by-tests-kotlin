package unit.auctionsniper

import auctionsniper.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.FeatureMatcher
import org.hamcrest.Matcher
import org.jmock.AbstractExpectations.returnValue
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test


class SniperLauncherTest {
    private val context = JUnit5Mockery()
    private val auctionState = context.states("auction state").startsAs("not joined")
    private val auction = context.mock(Auction::class.java)
    private val auctionHouse = context.mock(AuctionHouse::class.java)
    private val sniperCollector = context.mock(SniperCollector::class.java)
    private val launcher = SniperLauncher(auctionHouse, sniperCollector)

    @Test
    fun addsNewSniperToCollectorAndThenJoinsAuction() {
        val item = Item("itemId 123", 0)

        context.expect {
            allowing(auctionHouse).auctionFor(item)
            will(returnValue(auction))

            oneOf(auction).addAuctionEventListener(with(sniperForItem(item)))
            `when`(auctionState.`is`("not joined"))
            oneOf(sniperCollector).addSniper(with(sniperForItem(item)))
            `when`(auctionState.`is`("not joined"))

            oneOf(auction).join()
            then(auctionState.`is`("joined"))
        }.whenRunning {
            launcher.joinAuction(item)
        }
    }

    private fun sniperForItem(item: Item): Matcher<AuctionSniper> {
        return object : FeatureMatcher<AuctionSniper, Item>(equalTo(item), "sniper with itemId id", "itemId") {
            override fun featureValueOf(actual: AuctionSniper): Item {
                return actual.snapshot.item
            }
        }
    }
}