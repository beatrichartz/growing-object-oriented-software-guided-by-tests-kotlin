package auctionsniper

import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel(), SniperListener {
    companion object {
        fun textFor(state: SniperState): String {
            return STATUS_TEXT[state.ordinal]
        }

        private val STATUS_TEXT = arrayOf(
                "Joining", "Bidding", "Winning", "Lost", "Won"
        )
    }

    private val snapshots: ArrayList<SniperSnapshot> = arrayListOf()
    fun addSniper(snapshot: SniperSnapshot) {
        snapshots.add(snapshot)
        val rowIndex = snapshots.size - 1
        fireTableRowsInserted(rowIndex, rowIndex)
    }

    override fun getRowCount(): Int {
        return snapshots.size
    }

    override fun getColumnCount(): Int {
        return Column.values().size
    }

    override fun getColumnName(column: Int): String {
        return Column.at(column).title
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return Column.at(columnIndex).valueIn(snapshots[rowIndex])
    }

    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        val row = snapshots.indexOfFirst { snapshot.itemId == it.itemId }
        if (row < 0) throw IllegalArgumentException("Sniper for ${snapshot.itemId} is not registered")

        snapshots[row] = snapshot
        fireTableRowsUpdated(row, row)
    }
}