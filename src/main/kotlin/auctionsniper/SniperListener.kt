package auctionsniper

import java.util.*

interface SniperListener : EventListener {
    fun sniperStateChanged(snapshot: SniperSnapshot)
}
