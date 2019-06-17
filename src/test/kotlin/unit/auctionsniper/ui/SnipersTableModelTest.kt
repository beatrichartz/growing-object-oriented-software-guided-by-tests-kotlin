package unit.auctionsniper.ui

import auctionsniper.ui.Column
import auctionsniper.SniperSnapshot
import auctionsniper.SniperState
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SnipersTableModel.Companion.textFor
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.jmock.junit5.JUnit5Mockery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import unit.auctionsniper.expect
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener


class SnipersTableModelTest {
    private val context = JUnit5Mockery()
    private val listener = context.mock(TableModelListener::class.java)
    private val model = SnipersTableModel()

    @BeforeEach
    internal fun attachModelListener() {
        model.addTableModelListener(listener)
    }

    @Test
    internal fun hasEnoughColumns() {
        assertThat(model.columnCount, equalTo(Column.values().size))
    }

    @Test
    internal fun setsUpColumnHeadings() {
        for (column in Column.values()) {
            assertEquals(column.title, model.getColumnName(column.ordinal))
        }
    }

    @Test
    internal fun notifiesListenersWhenAddingASniper() {
        val joining = SniperSnapshot.joining("item-321")
        assertEquals(0, model.rowCount)

        context.expect {
            oneOf(listener).tableChanged(with(anInsertionAtRow(0)))
        }.whenRunning {
            model.addSniper(joining)
        }

        assertEquals(1, model.rowCount)
        assertRowMatchesSnapshot(0, joining)
    }

    @Test
    internal fun holdsSnipersInAdditionOrder() {
        context.expect {
            ignoring(listener)
        }.whenRunning {
            model.addSniper(SniperSnapshot.joining("item 0"))
            model.addSniper(SniperSnapshot.joining("item 1"))
        }

        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER))
        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER))
    }

    @Test
    internal fun updatesCorrectRowForSniper() {
        context.expect {
            allowing(listener).tableChanged(with(anyInsertionEvent()))
            oneOf(listener).tableChanged(with(aChangeInRow(1)))
        }.whenRunning {
            val joining = SniperSnapshot.joining("item 0")
            val joining2 = SniperSnapshot.joining("item 1")

            model.addSniper(joining)
            model.addSniper(joining2)
            model.sniperStateChanged(joining2.bidding(555, 666))
        }

        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER))
        assertEquals(textFor(SniperState.JOINING), cellValue(0, Column.SNIPER_STATE))

        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER))
        assertEquals(555, cellValue(1, Column.LAST_PRICE))
        assertEquals(666, cellValue(1, Column.LAST_BID))
        assertEquals(textFor(SniperState.BIDDING), cellValue(1, Column.SNIPER_STATE))
    }

    @Test
    internal fun throwsDefectIfNoExistingSniperForAnUpdate() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            model.sniperStateChanged(
                    SniperSnapshot.joining("item").bidding(444,445))
        }

        assertThat(exception, hasProperty("message", containsString("item")))
    }

    private fun assertRowMatchesSnapshot(row: Int, snapshot: SniperSnapshot) {
        assertEquals(snapshot.itemId, cellValue(row, Column.ITEM_IDENTIFIER))
        assertEquals(snapshot.lastPrice, cellValue(row, Column.LAST_PRICE))
        assertEquals(snapshot.lastBid, cellValue(row, Column.LAST_BID))
        assertEquals(textFor(snapshot.state), cellValue(row, Column.SNIPER_STATE))
    }

    private fun cellValue(rowIndex: Int, column: Column): Any {
        return model.getValueAt(rowIndex, column.ordinal)
    }

    private fun anyInsertionEvent(): Matcher<TableModelEvent> {
        return hasProperty("type", equalTo(TableModelEvent.INSERT));
    }

    private fun anInsertionAtRow(row: Int): Matcher<TableModelEvent> {
        return samePropertyValuesAs(TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT))
    }

    private fun aChangeInRow(row: Int): Matcher<TableModelEvent> {
        return samePropertyValuesAs(TableModelEvent(model, row))
    }
}
