package integration.auctionsniper

import auctionsniper.MainWindow
import auctionsniper.SnipersTableModel
import auctionsniper.UserRequestListener
import com.objogate.wl.swing.probe.ValueMatcherProbe
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import support.auctionsniper.AuctionSniperDriver



class MainWindowTest {
    private val tableModel = SnipersTableModel()
    private val mainWindow = MainWindow(tableModel)
    private val driver = AuctionSniperDriver(100)

    @Test
    internal fun makesUserRequestWhenJoinButtonClicked() {
        val buttonProbe = ValueMatcherProbe<String>(equalTo("an item id"), "join request")


        mainWindow.addUserRequestListener(object : UserRequestListener {
            override fun joinAuction(itemId: String) {
                buttonProbe.setReceivedValue(itemId)
            }
        })

        driver.startBiddingFor("an item id")
        driver.check(buttonProbe)
    }
}