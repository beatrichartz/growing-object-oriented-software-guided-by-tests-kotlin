package auctionsniper.ui

import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import auctionsniper.SniperPortfolio.PortfolioListener
import auctionsniper.SniperSnapshot
import auctionsniper.SniperState
import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel(), SniperListener, PortfolioListener {
    companion object {
        fun textFor(state: SniperState): String {
            return STATUS_TEXT[state.ordinal]
        }

        private val STATUS_TEXT = arrayOf(
                "Joining", "Bidding", "Winning", "Losing", "Lost", "Won"
        )
    }

    private val snapshots: ArrayList<SniperSnapshot> = arrayListOf()

    override fun sniperAdded(sniper: AuctionSniper) {
        sniper.addSniperListener(SwingThreadSniperListener(this))
        addSniperSnapshot(sniper.snapshot)
    }

    private fun addSniperSnapshot(snapshot: SniperSnapshot) {
        val row = snapshots.size
        snapshots.add(snapshot)
        fireTableRowsInserted(row, row)
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