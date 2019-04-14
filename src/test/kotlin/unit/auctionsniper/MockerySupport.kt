package unit.auctionsniper

import org.jmock.Expectations
import org.jmock.junit5.JUnit5Mockery

internal fun JUnit5Mockery.expect(expectations: Expectations.() -> Unit) = RunClause(this, expectations)

internal class RunClause(private val mockery: JUnit5Mockery, private val expectations: Expectations.() -> Unit) {
    fun whenRunning(block: () -> Unit) {
        mockery.checking(Expectations().apply(expectations))
        block()
        mockery.assertIsSatisfied()
    }
}