package auctionsniper.ui

import auctionsniper.UserRequestListener
import eventhandling.Announcer
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*

class MainWindow(private val snipers: SnipersTableModel) : JFrame(APPLICATION_TITLE) {
    companion object {
        const val NEW_ITEM_ID_NAME = "item id"
        const val JOIN_BUTTON_NAME = "join"
        const val APPLICATION_TITLE = "Auction Sniper"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPERS_TABLE_NAME = "Snipers"
    }

    private val userRequests = Announcer.toListenerType(UserRequestListener::class.java)

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable(), makeControls())
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

        val joinAuctionButton = JButton("Join Auction")
        joinAuctionButton.name = JOIN_BUTTON_NAME
        joinAuctionButton.addActionListener {
            userRequests.announce().joinAuction(itemIdField.text)
        }
        controls.add(joinAuctionButton)

        return controls
    }

    private fun makeSnipersTable(): JTable {
        val snipersTable = JTable(snipers)
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

