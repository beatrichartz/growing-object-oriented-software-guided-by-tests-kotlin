package endtoend.auctionsniper

import auctionsniper.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.*
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import org.hamcrest.CoreMatchers.any
import javax.swing.JButton
import javax.swing.JTextField
import javax.swing.table.JTableHeader

class AuctionSniperDriver(timeoutMillis: Long): JFrameDriver(
        GesturePerformer(),
        JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), ComponentDriver.showingOnScreen()),
        AWTEventQueueProber(timeoutMillis, 100)
) {
    fun showsSniperStatus(itemId: String, statusText: String) {
        JTableDriver(this).hasRow(
                matching(withLabelText(itemId), withLabelText(any(String::class.java)),
                        withLabelText(any(String::class.java)), withLabelText(statusText)))
    }

    fun showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, statusText: String) {
        JTableDriver(this).hasRow(
                matching(withLabelText(itemId), withLabelText(lastPrice.toString()),
                        withLabelText(lastBid.toString()), withLabelText(statusText)))
    }

    fun hasColumnTitles() {
        val headers = JTableHeaderDriver(this, JTableHeader::class.java)
        headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"),
                withLabelText("Last Bid"), withLabelText("State")))
    }

    fun startBiddingFor(itemId: String) {
        itemIdField().replaceAllText(itemId)
        bidButton().click()
    }

    private fun bidButton(): JButtonDriver {
        return JButtonDriver(this, JButton::class.java, named(MainWindow.JOIN_BUTTON_NAME))
    }

    private fun itemIdField(): JTextFieldDriver {
        val newItemId = JTextFieldDriver(this, JTextField::class.java, named(MainWindow.NEW_ITEM_ID_NAME))
        newItemId.focusWithMouse()
        return newItemId
    }
}
