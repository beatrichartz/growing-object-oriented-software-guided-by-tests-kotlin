package auctionsniper

import java.awt.Color
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.border.LineBorder

class MainWindow : JFrame("Auction Sniper") {
    companion object {
        const val STATUS_JOINING = "Joining"
        const val STATUS_LOST = "Lost"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPER_STATUS_NAME = "sniper status"
    }

    private val sniperStatus = createLabel(STATUS_JOINING)

    init {
        name = MAIN_WINDOW_NAME
        add(sniperStatus)
        pack()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

    private fun createLabel(initialText: String): JLabel {
        val result = JLabel(initialText)
        result.name = SNIPER_STATUS_NAME
        result.border = LineBorder(Color.BLACK)
        return result
    }
}