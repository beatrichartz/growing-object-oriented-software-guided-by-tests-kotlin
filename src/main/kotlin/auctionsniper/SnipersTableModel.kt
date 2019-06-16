package auctionsniper

import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel() {
    companion object {
        fun textFor(state: SniperState): String {
            return STATUS_TEXT[state.ordinal]
        }

        val STARTING_UP = SniperSnapshot("", 0, 0, SniperState.JOINING)
        private val STATUS_TEXT = arrayOf(
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
        return Column.at(columnIndex).valueIn(snapshot)
    }

    fun sniperStateChanged(newSnapshot: SniperSnapshot) {
        snapshot = newSnapshot
        fireTableRowsUpdated(0, 0)
    }
}