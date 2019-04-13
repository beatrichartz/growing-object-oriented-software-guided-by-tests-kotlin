package auctionsniper

import auctionsniper.Main.Companion.MAIN_WINDOW_NAME
import javax.swing.JFrame

class MainWindow : JFrame("Auction Sniper") {
    init {
        name = MAIN_WINDOW_NAME
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
}