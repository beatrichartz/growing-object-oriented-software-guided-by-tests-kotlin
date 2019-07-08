package integration.auctionsniper

import auctionsniper.SniperPortfolio
import auctionsniper.UserRequestListener
import auctionsniper.ui.MainWindow
import com.objogate.wl.swing.probe.ValueMatcherProbe
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import support.auctionsniper.AuctionSniperDriver



class MainWindowTest {
    private val portfolio = SniperPortfolio()
    private val mainWindow = MainWindow(portfolio)
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

    @AfterEach
    internal fun tearDown() {
        driver.dispose()
    }
}