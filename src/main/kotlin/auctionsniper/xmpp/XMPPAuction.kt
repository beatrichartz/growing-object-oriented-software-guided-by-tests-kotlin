package auctionsniper.xmpp

import auctionsniper.Auction
import auctionsniper.AuctionEventListener
import auctionsniper.Item
import eventhandling.Announcer
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException

class XMPPAuction(connection: XMPPConnection, item: Item) : Auction {
    companion object {
        private const val ITEM_ID_AS_LOGIN = "auction-%s"
        private const val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/Auction"
    }

    private val chat: Chat
    private val auctionEventListeners: Announcer<AuctionEventListener> =
            Announcer.toListenerType(AuctionEventListener::class.java)

    init {
        chat = connection.chatManager.createChat(
                auctionId(item.identifier, connection),
                AuctionMessageTranslator(connection.user, auctionEventListeners.announce()))
    }

    override fun addAuctionEventListener(auctionEventListener: AuctionEventListener) {
        auctionEventListeners.addListener(auctionEventListener)
    }

    override fun bid(amount: Int) {
        sendMessage(SOLProtocol.bidCommand(amount))
    }

    override fun join() {
        sendMessage(SOLProtocol.joinCommand())
    }

    private fun sendMessage(message: String) {
        try {
            chat.sendMessage(message)
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }

    private fun auctionId(itemId: String, connection: XMPPConnection): String {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.serviceName)
    }
}