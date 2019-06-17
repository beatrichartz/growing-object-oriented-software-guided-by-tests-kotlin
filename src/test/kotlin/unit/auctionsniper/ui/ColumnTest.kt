package unit.auctionsniper.ui

import auctionsniper.SniperSnapshot
import auctionsniper.SniperState
import auctionsniper.ui.Column
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test

class ColumnTest {
    @Test
    internal fun columnsAreAtDefinedIndices() {
        assertEquals(Column.ITEM_IDENTIFIER, Column.at(0))
        assertEquals(Column.LAST_PRICE, Column.at(1))
        assertEquals(Column.LAST_BID, Column.at(2))
        assertEquals(Column.SNIPER_STATE, Column.at(3))
    }

    @Test
    internal fun retrievingSnapshotValuesForColumns() {
        val snapshot = SniperSnapshot("ID", 1, 2, SniperState.WON)
        assertEquals("ID", Column.ITEM_IDENTIFIER.valueIn(snapshot))
        assertEquals(1, Column.LAST_PRICE.valueIn(snapshot))
        assertEquals(2, Column.LAST_BID.valueIn(snapshot))
        assertEquals("Won", Column.SNIPER_STATE.valueIn(snapshot))
    }
}