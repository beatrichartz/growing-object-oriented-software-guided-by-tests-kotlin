package auctionsniper

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

class MainWindow(private val snipers: SnipersTableModel) : JFrame(APPLICATION_TITLE) {
    companion object {
        const val NEW_ITEM_ID_NAME = "item id"
        const val JOIN_BUTTON_NAME = "join"
        const val APPLICATION_TITLE = "Auction Sniper"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPERS_TABLE_NAME = "Snipers"
    }

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable())
        pack()
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }

    private fun makeSnipersTable(): JTable {
        val snipersTable = JTable(snipers)
        snipersTable.name = SNIPERS_TABLE_NAME
        return snipersTable
    }

    private fun fillContentPane(snipersTable: JTable) {
        contentPane.layout = BorderLayout()
        contentPane.add(JScrollPane(snipersTable), BorderLayout.CENTER)
    }
}

