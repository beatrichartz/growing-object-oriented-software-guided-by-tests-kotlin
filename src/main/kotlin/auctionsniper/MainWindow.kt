package auctionsniper

import auctionsniper.MainWindow.Companion.STATUS_JOINING
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.border.LineBorder
import javax.swing.table.AbstractTableModel

class MainWindow : JFrame(APPLICATION_TITLE) {
    companion object {
        const val STATUS_JOINING = "Joining"
        const val STATUS_BIDDING = "Bidding"
        const val STATUS_WINNING = "Winning"
        const val STATUS_WON = "Won"
        const val STATUS_LOST = "Lost"
        const val APPLICATION_TITLE = "Auction Sniper"
        const val MAIN_WINDOW_NAME = "Auction Sniper Main"
        const val SNIPER_STATUS_NAME = "sniper status"
        const val SNIPERS_TABLE_NAME = "Snipers"
    }

    private val sniperStatus = createLabel(STATUS_JOINING)
    private val snipers = SnipersTableModel()

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable())
        add(sniperStatus)
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

    private fun createLabel(initialText: String): JLabel {
        val result = JLabel(initialText.toString())
        result.name = SNIPER_STATUS_NAME
        result.border = LineBorder(Color.BLACK)
        return result
    }

    fun showStatus(status: String) {
        snipers.statusText = status
    }
}

class SnipersTableModel : AbstractTableModel() {
    var statusText = STATUS_JOINING
        set(value) {
            field = value
            fireTableRowsUpdated(0, 0)
        }

    override fun getRowCount(): Int {
        return 1
    }

    override fun getColumnCount(): Int {
        return 1
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): String {
        return statusText
    }
}
