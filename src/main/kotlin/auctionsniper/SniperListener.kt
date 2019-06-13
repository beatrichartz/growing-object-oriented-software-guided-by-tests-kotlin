package auctionsniper

import java.util.*

interface SniperListener : EventListener {
    fun sniperLost()
    fun sniperBidding(sniperState: SniperState)
    fun sniperWinning()
    fun sniperWon()
}
