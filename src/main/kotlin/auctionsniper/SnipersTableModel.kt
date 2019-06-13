package auctionsniper

import javax.swing.table.AbstractTableModel

enum class Column {
    ITEM_IDENTIFIER, LAST_PRICE, LAST_BID, SNIPER_STATUS;

    companion object {
        fun at(index: Int): Column {
            return values()[index]
        }
    }
}

class SnipersTableModel : AbstractTableModel() {
    companion object {
        val STARTING_UP = SniperState("", 0, 0)
    }

    private var sniperState = STARTING_UP
    internal var statusText = MainWindow.STATUS_JOINING

    override fun getRowCount(): Int {
        return 1
    }

    override fun getColumnCount(): Int {
        return Column.values().size
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when(Column.at(columnIndex)) {
            Column.ITEM_IDENTIFIER -> sniperState.itemId
            Column.LAST_PRICE -> sniperState.lastPrice
            Column.LAST_BID -> sniperState.lastBid
            Column.SNIPER_STATUS -> statusText
        }
    }

    fun sniperStatusChanged(newSniperState: SniperState, newStatusText: String) {
        sniperState = newSniperState
        statusText = newStatusText
        fireTableRowsUpdated(0, 0)
    }
}