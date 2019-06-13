package unit.auctionsniper

import auctionsniper.MainWindow
import auctionsniper.SniperState
import auctionsniper.SnipersTableModel
import auctionsniper.SnipersTableModel.Column
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.samePropertyValuesAs
import org.jmock.junit5.JUnit5Mockery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    internal fun setsSniperValuesInColumns() {
        context.expect {
            oneOf(listener).tableChanged(with(aRowChangedEvent()))
        }.whenRunning {
            model.sniperStatusChanged(
                    SniperState("item id", 555, 666),
                    MainWindow.STATUS_BIDDING)
        }

        assertColumnEquals(Column.ItemIdentifier, "item id")
    }

    private fun assertColumnEquals(column: Column, expected: Any) {
        val rowIndex = 0
        val columnIndex = column.ordinal

        assertEquals(expected, model.getValueAt(rowIndex, columnIndex))
    }

    private fun aRowChangedEvent(): Matcher<TableModelEvent>? {
        return samePropertyValuesAs(TableModelEvent(model, 0))
    }
}
