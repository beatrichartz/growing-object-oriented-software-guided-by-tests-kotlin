package auctionsniper

import javax.swing.table.AbstractTableModel

enum class Column {
    ITEM_IDENTIFIER, LAST_PRICE, LAST_BID, SNIPER_STATE;

    companion object {
        fun at(index: Int): Column {
            return values()[index]
        }
    }
}

class SnipersTableModel : AbstractTableModel() {
    companion object {
        val STARTING_UP = SniperSnapshot("", 0, 0, SniperState.JOINING)
        val STATUS_TEXT = arrayOf(
                "Joining", "Bidding", "Winning", "Lost", "Won"
        )
    }

    private var snapshot = STARTING_UP

    override fun getRowCount(): Int {
        return 1
    }

    override fun getColumnCount(): Int {
        return Column.values().size
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when(Column.at(columnIndex)) {
            Column.ITEM_IDENTIFIER -> snapshot.itemId
            Column.LAST_PRICE -> snapshot.lastPrice
            Column.LAST_BID -> snapshot.lastBid
            Column.SNIPER_STATE -> textFor(snapshot.state)
        }
    }

    private fun textFor(state: SniperState): String {
        return STATUS_TEXT[state.ordinal]
    }

    fun sniperStateChanged(newSnapshot: SniperSnapshot) {
        snapshot = newSnapshot
        fireTableRowsUpdated(0, 0)
    }
}