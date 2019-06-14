package unit.auctionsniper

import auctionsniper.SniperSnapshot
import auctionsniper.SniperState
import org.hamcrest.Matcher
import org.jmock.Expectations
import org.jmock.junit5.JUnit5Mockery

internal class NonNullExpectations : Expectations() {
    internal fun with(matcher: Matcher<SniperSnapshot>): SniperSnapshot {
        val nullSniperSnapshot = SniperSnapshot("", 0, 0, SniperState.JOINING)
        with(matcher, nullSniperSnapshot)
        return nullSniperSnapshot
    }

    internal fun <T : Any> with(matcher: Matcher<T>, nullObject: T): T {
        with(matcher)
        return nullObject
    }

}
internal fun JUnit5Mockery.expect(expectations: NonNullExpectations.() -> Unit) = RunClause(this, expectations)

internal class RunClause(private val mockery: JUnit5Mockery, private val expectations: NonNullExpectations.() -> Unit) {
    fun whenRunning(block: () -> Unit) {
        mockery.checking(NonNullExpectations().apply(expectations))
        block()
        mockery.assertIsSatisfied()
    }
}

