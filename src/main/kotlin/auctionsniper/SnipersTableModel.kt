package auctionsniper

import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel(), SniperListener {
    companion object {
        fun textFor(state: SniperState): String {
            return STATUS_TEXT[state.ordinal]
        }

        private val STARTING_UP = SniperSnapshot("", 0, 0, SniperState.JOINING)
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

    override fun getColumnName(column: Int): String {
        return Column.at(column).title
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return Column.at(columnIndex).valueIn(snapshot)
    }

    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        this.snapshot = snapshot
        fireTableRowsUpdated(0, 0)
    }
}