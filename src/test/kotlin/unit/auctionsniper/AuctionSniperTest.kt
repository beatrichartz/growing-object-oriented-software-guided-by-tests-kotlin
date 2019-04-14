package unit.auctionsniper

import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test

class AuctionSniperTest {
    private val context = JUnit5Mockery()
    private val sniperListener = context.mock(SniperListener::class.java)
    private val sniper = AuctionSniper(sniperListener)

    @Test
    fun reportsLostWhenAuctionCloses() {
        context.expect {
            oneOf(sniperListener).sniperLost()
        }.whenRunning {
            sniper.auctionClosed()
        }
    }
}