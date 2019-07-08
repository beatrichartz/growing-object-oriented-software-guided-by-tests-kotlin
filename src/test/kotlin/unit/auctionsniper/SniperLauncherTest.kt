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
        val itemId = "item 123"

        context.expect {
            allowing(auctionHouse).auctionFor(itemId)
            will(returnValue(auction))

            oneOf(auction).addAuctionEventListener(with(sniperForItem(itemId)))
            `when`(auctionState.`is`("not joined"))
            oneOf(sniperCollector).addSniper(with(sniperForItem(itemId)))
            `when`(auctionState.`is`("not joined"))

            oneOf(auction).join()
            then(auctionState.`is`("joined"))
        }.whenRunning {
            launcher.joinAuction(itemId)
        }
    }

    private fun sniperForItem(itemId: String): Matcher<AuctionSniper> {
        return object : FeatureMatcher<AuctionSniper, String>(equalTo(itemId), "sniper with itemId id", "itemId") {
            override fun featureValueOf(actual: AuctionSniper): String {
                return actual.snapshot.itemId
            }
        }
    }
}