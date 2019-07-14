package auctionsniper.xmpp

import auctionsniper.Auction
import auctionsniper.AuctionHouse
import auctionsniper.Item
import org.jivesoftware.smack.XMPPConnection

class XMPPAuctionHouse(private val connection: XMPPConnection) : AuctionHouse {
    companion object {
        private const val AUCTION_RESOURCE = "Auction"

        fun connect(hostname: String, username: String, password: String): AuctionHouse {
            val connection = XMPPConnection(hostname)
            connection.connect()
            connection.login(username, password, AUCTION_RESOURCE)

            return XMPPAuctionHouse(connection)
        }
    }

    override fun auctionFor(item: Item): Auction {
        return XMPPAuction(connection, item)
    }

    override fun disconnect() {
        connection.disconnect()
    }

}
