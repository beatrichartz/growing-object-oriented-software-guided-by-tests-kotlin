package auctionsniper

import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SwingThreadSniperListener

class SniperLauncher(private val auctionHouse: AuctionHouse,
                     private val snipers: SnipersTableModel) : UserRequestListener {
    override fun joinAuction(itemId: String) {
        snipers.addSniper(SniperSnapshot.joining(itemId))
        val auction = auctionHouse.auctionFor(itemId)
        auction.addAuctionEventListener(
                AuctionSniper(itemId, auction, SwingThreadSniperListener(snipers)))
        auction.join()
    }
}
