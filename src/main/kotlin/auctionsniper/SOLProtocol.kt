package auctionsniper

object SOLProtocol {
    private const val VERSION = "SOLVersion: 1.1;"

    private fun command(command: String): String {
        return "$VERSION Command: $command;"
    }

    private fun event(event: String): String {
        return "$VERSION Event: $event;"
    }

    fun bidCommand(price: Int): String {
        return "${command("BID")} Price: $price;"
    }

    fun joinCommand(): String {
        return command("JOIN")
    }

    fun priceEvent(price: Int, increment: Int, bidder: String): String {
        return "${event("PRICE")} CurrentPrice: $price; Increment: $increment; Bidder: $bidder;"
    }

    fun closeEvent(): String {
        return event("CLOSE")
    }
}