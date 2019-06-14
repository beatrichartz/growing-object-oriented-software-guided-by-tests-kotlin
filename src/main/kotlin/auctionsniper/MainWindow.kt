package auctionsniper

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

class MainWindow : JFrame(APPLICATION_TITLE) {
    companion object {
        const val APPLICATION_TITLE = "Auction Sniper"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPERS_TABLE_NAME = "Snipers"
    }

    private val snipers = SnipersTableModel()

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

    fun sniperStateChanged(sniperSnapshot: SniperSnapshot) {
        snipers.sniperStateChanged(sniperSnapshot)
    }
}

