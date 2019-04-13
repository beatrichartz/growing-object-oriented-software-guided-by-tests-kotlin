package endtoend.auctionsniper

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import java.lang.String.format
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

class FakeAuctionServer(itemId: String) {

    companion object {
        const val ITEM_ID_AS_LOGIN = "auction-%s"
        const val AUCTION_RESOURCE = "Auction"
        const val XMPP_HOSTNAME = "localhost"
        const val AUCTION_PASSWORD = "auction"
    }

    internal val itemId: String = itemId
    private val connection: XMPPConnection
    private val messageListener: SingleMessageListener = SingleMessageListener()
    private var currentChat: Chat? = null

    init {
        connection = XMPPConnection(XMPP_HOSTNAME)
    }

    fun startSellingItem() {
        connection.connect()
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
        connection.chatManager.addChatListener { chat: Chat, _: Boolean ->
            currentChat = chat
            chat.addMessageListener(messageListener)
        }
    }

    fun hasReceivedJoinRequestFromSniper() {
        messageListener.receivesAMessage()
    }

    fun announceClosed() {
        currentChat?.sendMessage(Message())
    }

    fun stop() {
        connection.disconnect()
    }
}


class SingleMessageListener(
        private val messages: ArrayBlockingQueue<Message> = ArrayBlockingQueue(1)
) : MessageListener {

    override fun processMessage(chat: Chat?, message: Message?) {
        messages.add(message)
    }

    fun receivesAMessage() {
        MatcherAssert.assertThat("Message", messages.poll(5, TimeUnit.SECONDS), CoreMatchers.notNullValue())
    }

}