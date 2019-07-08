package auctionsniper

import eventhandling.Announcer
import java.util.*

class SniperPortfolio : SniperCollector {
    private val snipers = ArrayList<AuctionSniper>()
    private val announcer = Announcer.toListenerType(PortfolioListener::class.java)

    interface PortfolioListener : EventListener {
        fun sniperAdded(sniper: AuctionSniper)
    }

    override fun addSniper(sniper: AuctionSniper) {
        snipers.add(sniper)
        announcer.announce().sniperAdded(sniper)
    }

    fun addPortfolioListener(listener: PortfolioListener) {
        announcer.addListener(listener)
    }
}