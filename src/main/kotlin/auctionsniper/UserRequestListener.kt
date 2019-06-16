package auctionsniper

import java.util.*

interface UserRequestListener: EventListener {
    fun joinAuction(itemId: String)
}