package endtoend.auctionsniper

import auctionsniper.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.ComponentDriver
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.gesture.GesturePerformer
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
}