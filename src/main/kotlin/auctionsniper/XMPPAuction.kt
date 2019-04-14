package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPException

class XMPPAuction(private val chat: Chat) : Auction {
    override fun bid(amount: Int) {
        sendMessage(SOLProtocol.bidCommand(amount))
    }

    fun join() {
        sendMessage(SOLProtocol.joinCommand())
    }

    private fun sendMessage(message: String) {
        try {
            chat.sendMessage(message)
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }
}