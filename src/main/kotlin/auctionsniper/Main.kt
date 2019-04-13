package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import javax.swing.SwingUtilities

class Main {
    companion object {
        private const val ARG_HOSTNAME = 0
        private const val ARG_USERNAME = 1
        private const val ARG_PASSWORD = 2
        private const val ARG_ITEM_ID = 3

        private const val AUCTION_RESOURCE = "Auction"
        private const val ITEM_ID_AS_LOGIN = "auction-%s"
        private const val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

        fun main(vararg args: String) {
            val main = Main()

            main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),
                    args[ARG_ITEM_ID])
        }

        private fun connection(hostname: String, username: String, password: String): XMPPConnection {
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

    private fun joinAuction(connection: XMPPConnection, itemId: String) {
        val chat = connection.chatManager.createChat(
                auctionId(itemId, connection)
        ) { _: Chat, _: Message ->
            SwingUtilities.invokeLater {
                ui.showStatus(MainWindow.STATUS_LOST)
            }
        }

        chat.sendMessage(Message())
    }


    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow()
        }
    }
}

