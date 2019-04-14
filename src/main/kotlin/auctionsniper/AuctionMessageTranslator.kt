package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message
import java.util.*

class AuctionMessageTranslator(listener: AuctionEventListener) : MessageListener {
    private val listener = listener

    override fun processMessage(chat: Chat?, message: Message) {
        val event = unpackEventFrom(message)
        val type = event["Event"]
        if ("CLOSE" == type) {
            listener.auctionClosed()
        } else if ("PRICE" == type) {
            listener.currentPrice(Integer.parseInt(event["CurrentPrice"]),
                    Integer.parseInt(event["Increment"]))
        }
    }

    private fun unpackEventFrom(message: Message): HashMap<String, String> {
        val event = HashMap<String, String>()
        for (element in message.body.split(";").dropLastWhile { it.isEmpty() }) {
            val pair = element.split(":").map { it.trim() }
            event[pair[0]] = pair[1]
        }

        return event
    }
}