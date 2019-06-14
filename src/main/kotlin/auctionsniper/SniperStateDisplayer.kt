package auctionsniper

import javax.swing.SwingUtilities

class SniperStateDisplayer(private val ui: MainWindow) : SniperListener {
    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        SwingUtilities.invokeLater {
            ui.sniperStateChanged(snapshot)
        }
    }
}