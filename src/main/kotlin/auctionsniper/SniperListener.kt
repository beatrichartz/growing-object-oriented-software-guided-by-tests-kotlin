package auctionsniper

import java.util.*

interface SniperListener : EventListener {
    fun sniperLost()
    fun sniperBidding()
    fun sniperWinning()
    fun sniperWon()
}
