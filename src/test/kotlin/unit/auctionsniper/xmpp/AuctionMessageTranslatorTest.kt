package unit.auctionsniper.xmpp

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionEventListener.PriceSource
import auctionsniper.xmpp.AuctionMessageTranslator
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import unit.auctionsniper.expect
import org.jmock.Expectations



class AuctionMessageTranslatorTest {
    companion object {
        val UNUSED_CHAT: Chat? = null
        const val SNIPER_ID = "sniper"
    }

    @RegisterExtension
    val context = JUnit5Mockery()
    private val listener = context.mock(AuctionEventListener::class.java)
    private val translator = AuctionMessageTranslator(SNIPER_ID, listener)

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
            message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: $SNIPER_ID;"

            translator.processMessage(UNUSED_CHAT, message)
        }
    }

    @Test
    internal fun notifiesAuctionFailedWhenBadMessageReceived() {
        context.expect {
            exactly(1).of(listener).auctionFailed()
        }.whenRunning {
            val message = Message()
            message.body = "a bad message"

            translator.processMessage(UNUSED_CHAT, message)
        }
    }

    @Test
    fun notifiesAuctionFailedWhenEventTypeMissing() {
        context.expect {
            exactly(1).of(listener).auctionFailed()
        }.whenRunning {
            val message = Message()
            message.body = ("SOLVersion: 1.1; CurrentPrice: 234; Increment: 5; Bidder: "
                    + SNIPER_ID + ";")

            translator.processMessage(UNUSED_CHAT, message)
        }
    }
}

