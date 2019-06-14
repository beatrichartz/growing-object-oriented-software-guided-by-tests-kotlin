package auctionsniper

import javax.swing.SwingUtilities

class SniperStateDisplayer(private val ui: MainWindow) : SniperListener {
    override fun sniperWon() {
        showStatus(MainWindow.STATUS_WON)
    }

    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        SwingUtilities.invokeLater {
            ui.sniperStateChanged(snapshot)
        }
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