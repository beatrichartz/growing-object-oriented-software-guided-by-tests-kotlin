package unit.auctionsniper

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionMessageTranslator
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import org.jmock.Expectations
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
        context.checking(Expectations().apply {
            oneOf(listener).auctionClosed()
        })

        val message = Message()
        message.body = "SOLVersion: 1.1; Event: CLOSE;"

        translator.processMessage(UNUSED_CHAT, message)

        context.assertIsSatisfied()
    }

}

