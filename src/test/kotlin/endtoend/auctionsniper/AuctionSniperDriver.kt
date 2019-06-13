package endtoend.auctionsniper

import auctionsniper.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.ComponentDriver
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import org.hamcrest.CoreMatchers.equalTo

class AuctionSniperDriver(timeoutMillis: Long): JFrameDriver(
        GesturePerformer(),
        JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), ComponentDriver.showingOnScreen()),
        AWTEventQueueProber(timeoutMillis, 100)
) {
    fun showsSniperStatus(statusText: String) {
        JTableDriver(this).hasCell(withLabelText(equalTo(statusText)))
    }

    fun showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, statusText: String) {
        JTableDriver(this).hasRow(
                matching(withLabelText(itemId), withLabelText(lastPrice.toString()),
                        withLabelText(lastBid.toString()), withLabelText(statusText)))
    }
}
