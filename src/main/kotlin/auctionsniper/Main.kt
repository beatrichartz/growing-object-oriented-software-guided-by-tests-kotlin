package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SwingThreadSniperListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    companion object {
        private const val ARG_HOSTNAME = 0
        private const val ARG_USERNAME = 1
        private const val ARG_PASSWORD = 2

        fun main(vararg args: String) {
            val main = Main()
            var auctionHouse = XMPPAuctionHouse.connect(
                    args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD])
            main.disconnectWhenUICloses(auctionHouse)
            main.addUserRequestListenerFor(auctionHouse)
        }
    }

    private fun addUserRequestListenerFor(auctionHouse: AuctionHouse) {
        ui.addUserRequestListener(object : UserRequestListener {
            override fun joinAuction(itemId: String) {
                snipers.addSniper(SniperSnapshot.joining(itemId))
                val auction = auctionHouse.auctionFor(itemId)
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

    private fun disconnectWhenUICloses(auctionHouse: AuctionHouse) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                auctionHouse.disconnect()
            }
        })
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(snipers)
        }
    }
}

