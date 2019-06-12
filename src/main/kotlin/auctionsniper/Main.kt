package auctionsniper

import org.jivesoftware.smack.XMPPConnection
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
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
            connection.login(username, password, AUCTION_RESOURCE)

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
        disconnectWhenUICloses(connection)

        val chat = connection.chatManager.createChat(auctionId(itemId, connection), null)
        val auction = XMPPAuction(chat)

        chat.addMessageListener(AuctionMessageTranslator(connection.user, AuctionSniper(auction, SniperStateDisplayer(ui))))
        auction.join()
    }

    private fun disconnectWhenUICloses(connection: XMPPConnection) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                connection.disconnect()
            }
        })
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow()
        }
    }
}

