package unit.auctionsniper

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionEventListener.PriceSource
import auctionsniper.AuctionMessageTranslator
import endtoend.auctionsniper.ApplicationRunner.Companion.SNIPER_ID
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AuctionMessageTranslatorTest {
    companion object {
        val UNUSED_CHAT: Chat? = null
    }

    @RegisterExtension
    val context = JUnit5Mockery()
    private val listener = context.mock(AuctionEventListener::class.java)
    private val translator = AuctionMessageTranslator(listener)

    @Test
    internal fun notifiesAuctionClosedWhenCloseMessageReceived() {
        context.expect {
            oneOf(listener).auctionClosed()
        }.whenRunning {
            val message = Message()
            message.body = "SOLVersion: 1.1; Event: CLOSE;"

            translator.processMessage(UNUSED_CHAT, message)
        }
    }

    @Test
    internal fun notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        context.expect {
            exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder)
        }.whenRunning {
            val message = Message()
            message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"

            translator.processMessage(UNUSED_CHAT, message)
        }
    }

    @Test
    internal fun notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        context.expect {
            exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromSniper)
        }.whenRunning {
            val message = Message()
            message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: ${SNIPER_ID};"

            translator.processMessage(UNUSED_CHAT, message)
        }
    }
}

