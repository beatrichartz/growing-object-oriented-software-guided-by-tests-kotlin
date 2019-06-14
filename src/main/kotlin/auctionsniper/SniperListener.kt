package auctionsniper

import java.util.*

interface SniperListener : EventListener {
    fun sniperLost()
    fun sniperStateChanged(snapshot: SniperSnapshot)
    fun sniperWon()
}
