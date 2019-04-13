package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import javax.swing.SwingUtilities

class Main {
    companion object {
        const val ARG_HOSTNAME = 0
        const val ARG_USERNAME = 1
        const val ARG_PASSWORD = 2
        const val ARG_ITEM_ID = 3

        const val AUCTION_RESOURCE = "Auction"
        const val ITEM_ID_AS_LOGIN = "auction-%s"
        const val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

        fun main(vararg args: String) {
            Main()

            val connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD])
            val chat = connection.chatManager.createChat(
                    auctionId(args[ARG_ITEM_ID], connection)
            ) { _: Chat, _: Message ->
                // nothing yet
            }
            chat.sendMessage(Message())
        }

        private fun connectTo(hostname: String, username: String, password: String): XMPPConnection {
            val connection = XMPPConnection(hostname)
            connection.connect()
            connection.login(username, password)

            return connection
        }

        private fun auctionId(itemId: String, connection: XMPPConnection): String {
            return String.format(AUCTION_ID_FORMAT, itemId, connection.serviceName)
        }
    }

    private lateinit var ui: MainWindow

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow()
        }
    }
}
