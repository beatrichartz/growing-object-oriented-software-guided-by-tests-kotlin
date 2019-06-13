package unit.auctionsniper

import auctionsniper.SnipersTableModel
import org.hamcrest.Matchers.equalTo
import org.jmock.junit5.JUnit5Mockery
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.event.TableModelListener

class SnipersTableModelTest {
    private val context = JUnit5Mockery()
    private val tableModelListener = context.mock(TableModelListener::class.java)
    private val model = SnipersTableModel()

    @BeforeEach
    internal fun attachModelListener() {
        model.addTableModelListener(tableModelListener)
    }

    @Test
    internal fun hasEnoughColumns() {
        assertThat(model.columnCount, equalTo(SnipersTableModel.Column.values().size))
    }
}
