package auctionsniper.ui

import auctionsniper.SniperListener
import auctionsniper.SniperSnapshot
import javax.swing.SwingUtilities

class SwingThreadSniperListener(private val sniperListener: SniperListener) : SniperListener {
    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        SwingUtilities.invokeLater {
            sniperListener.sniperStateChanged(snapshot)
        }
    }
}