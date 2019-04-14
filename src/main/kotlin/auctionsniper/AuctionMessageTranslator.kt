package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator(listener: AuctionEventListener) : MessageListener {
    private val listener = listener

    override fun processMessage(chat: Chat?, message: Message) {
        listener.auctionClosed()
    }
}