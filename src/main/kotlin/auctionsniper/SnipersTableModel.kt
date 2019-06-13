package auctionsniper

import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel() {
    enum class Column {
        ItemIdentifier, LastPrice, LastBid, SniperStatus
    }

    var statusText = MainWindow.STATUS_JOINING
        set(value) {
            field = value
            fireTableRowsUpdated(0, 0)
        }

    override fun getRowCount(): Int {
        return 1
    }

    override fun getColumnCount(): Int {
        return 4
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): String {
        return statusText
    }

    fun sniperStatusChanged(sniperState: SniperState, statusText: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}