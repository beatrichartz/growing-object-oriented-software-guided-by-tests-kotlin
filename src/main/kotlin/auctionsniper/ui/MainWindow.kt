package auctionsniper.ui

import auctionsniper.SniperPortfolio
import auctionsniper.UserRequestListener
import eventhandling.Announcer
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.text.NumberFormat
import javax.swing.*



class MainWindow(portfolio: SniperPortfolio) : JFrame(APPLICATION_TITLE) {
    companion object {
        const val NEW_ITEM_STOP_PRICE_NAME = "stop price"
        const val NEW_ITEM_ID_NAME = "item id"
        const val JOIN_BUTTON_NAME = "join"
        const val APPLICATION_TITLE = "Auction Sniper"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPERS_TABLE_NAME = "Snipers"
    }

    private val userRequests = Announcer.toListenerType(UserRequestListener::class.java)

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable(portfolio), makeControls())
        pack()
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }

    private fun makeControls(): JPanel {
        val controls = JPanel(FlowLayout())
        val itemIdField = JTextField()
        itemIdField.columns = 25
        itemIdField.name = NEW_ITEM_ID_NAME
        controls.add(itemIdField)

        val stopPriceField = JFormattedTextField(NumberFormat.getIntegerInstance())
        stopPriceField.columns = 7
        stopPriceField.name = NEW_ITEM_STOP_PRICE_NAME
        controls.add(stopPriceField)

        val joinAuctionButton = JButton("Join Auction")
        joinAuctionButton.name = JOIN_BUTTON_NAME
        joinAuctionButton.addActionListener {
            userRequests.announce().joinAuction(itemIdField.text)
        }
        controls.add(joinAuctionButton)

        return controls
    }

    private fun makeSnipersTable(portfolio: SniperPortfolio): JTable {
        val model = SnipersTableModel()
        portfolio.addPortfolioListener(model)
        val snipersTable = JTable(model)
        snipersTable.name = SNIPERS_TABLE_NAME
        return snipersTable
    }

    private fun fillContentPane(snipersTable: JTable, controls: JPanel) {
        contentPane.layout = BorderLayout()
        contentPane.add(controls, BorderLayout.NORTH)
        contentPane.add(JScrollPane(snipersTable), BorderLayout.CENTER)
    }

    fun addUserRequestListener(userRequestListener: UserRequestListener) {
        userRequests.addListener(userRequestListener)
    }
}

