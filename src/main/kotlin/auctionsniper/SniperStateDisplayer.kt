package auctionsniper

import javax.swing.SwingUtilities

class SniperStateDisplayer(private val ui: MainWindow) : SniperListener {
    override fun sniperWon() {
        showStatus(MainWindow.STATUS_WON)
    }

    override fun sniperWinning() {
        showStatus(MainWindow.STATUS_WINNING)
    }

    override fun sniperBidding() {
        showStatus(MainWindow.STATUS_BIDDING)
    }

    override fun sniperLost() {
        showStatus(MainWindow.STATUS_LOST)
    }

    private fun showStatus(status: String) {
        SwingUtilities.invokeLater {
            ui.showStatus(status)
        }
    }

}