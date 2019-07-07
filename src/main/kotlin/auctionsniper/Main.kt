package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SwingThreadSniperListener
import auctionsniper.xmpp.XMPPAuction
import org.jivesoftware.smack.XMPPConnection
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    companion object {
        private const val ARG_HOSTNAME = 0
        private const val ARG_USERNAME = 1
        private const val ARG_PASSWORD = 2

        internal const val AUCTION_RESOURCE = "Auction"

        fun main(vararg args: String) {
            val main = Main()

            val connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD])
            main.disconnectWhenUICloses(connection)
            main.addUserRequestListenerFor(connection)
        }

        private fun connection(hostname: String, username: String, password: String): XMPPConnection {
            val connection = XMPPConnection(hostname)
            connection.connect()
            connection.login(username, password, AUCTION_RESOURCE)

            return connection
        }

    }

    private fun addUserRequestListenerFor(connection: XMPPConnection) {
        ui.addUserRequestListener(object : UserRequestListener {
            override fun joinAuction(itemId: String) {
                snipers.addSniper(SniperSnapshot.joining(itemId))
                val auction: Auction = XMPPAuction(connection, itemId)
                auction.addAuctionEventListener(
                        AuctionSniper(itemId, auction, SwingThreadSniperListener(snipers)))
                auction.join()
            }
        })
    }

    private val snipers = SnipersTableModel()
    private lateinit var ui: MainWindow

    init {
        startUserInterface()
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
            ui = MainWindow(snipers)
        }
    }
}

