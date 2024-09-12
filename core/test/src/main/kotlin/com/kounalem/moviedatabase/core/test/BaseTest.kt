package com.kounalem.moviedatabase.core.test

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.TurbineContext
import app.cash.turbine.turbineScope
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseTest {
    private val wrappers = mutableListOf<ReceiveTurbineWrapper<*>>()

    context(TurbineContext)
    protected fun <T> Flow<T>.testSubscribe(): ReceiveTurbineWrapper<T> =
        ReceiveTurbineWrapper(testIn(this@TurbineContext)).also { wrappers.add(it) }

    protected fun runTest(
        context: CoroutineContext = EmptyCoroutineContext,
        testBody: suspend context(TurbineContext, TestScope) () -> Unit,
    ) = kotlinx.coroutines.test.runTest(context = context) {
        val testScope = this
        turbineScope { testBody(this, testScope) }
    }

    @After
    fun baseCleanup() {
        wrappers.forEach {
            if (!it.cancelled) throw AssertionError("Wrapper $it has not been cancelled")
        }
    }

    companion object {
        @AfterClass
        @JvmStatic
        fun baseTestCleanup() {
            unmockkAll()
        }
    }
}

class ReceiveTurbineWrapper<T>(private val turbine: ReceiveTurbine<T>) {
    private val cache: MutableList<T> = mutableListOf()
    var cancelled = false

    suspend fun assertAt(idx: Int, assertion: (T) -> Unit): ReceiveTurbineWrapper<T> {
        if (cache.size <= idx) {
            repeat(idx - cache.size + 1) {
                cache.add(turbine.awaitItem())
            }
        }
        assertion(cache[idx])
        return this
    }

    suspend fun assertValueAt(idx: Int, check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        return assertAt(idx) { assertTrue(check(it)) }
    }

    suspend fun assertValueAt(idx: Int, item: T): ReceiveTurbineWrapper<T> {
        return assertAt(idx) { assertEquals(item, it) }
    }

    fun assertNever(check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        expectAllItems()
        assertFalse(cache.any(check))
        return this
    }

    suspend fun assertValue(check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        cache.add(turbine.awaitItem())
        assertTrue(check(cache.last()))
        return this
    }

    suspend fun assertValue(item: T): ReceiveTurbineWrapper<T> {
        cache.add(turbine.awaitItem())
        assertEquals(item, cache.last())
        return this
    }

    suspend fun assertValues(vararg items: T): ReceiveTurbineWrapper<T> {
        items.forEachIndexed { index, t -> assertValueAt(index, t) }
        return this
    }

    suspend fun awaitCount(count: Int) = assertValueCount(count)

    suspend fun assertValueCount(count: Int): ReceiveTurbineWrapper<T> {
        val remaining = count - cache.size
        if (remaining < 0) {
            throw AssertionError("Expected $count values but have ${cache.size}")
        } else if (remaining == 0) {
            turbine.expectNoEvents()
        } else {
            repeat(remaining) {
                cache.add(turbine.awaitItem())
            }
            turbine.expectNoEvents()
        }
        return this
    }

    fun assertLast(assertion: (T) -> Unit): ReceiveTurbineWrapper<T> {
        expectAllItems()
        assertion(cache.last())
        return this
    }

    fun values(): List<T> {
        expectAllItems()
        return cache
    }

    fun valueCount(): Int = values().size

    suspend fun assertEmpty(): ReceiveTurbineWrapper<T> = assertValueCount(0)
    suspend fun assertNoValues(): ReceiveTurbineWrapper<T> = assertValueCount(0)

    suspend fun dispose(): ReceiveTurbineWrapper<T> {
        turbine.cancelAndIgnoreRemainingEvents()
        cancelled = true
        return this
    }

    private fun expectAllItems() {
        try {
            val channel = turbine.asChannel()
            do {
                val current = channel.tryReceive().getOrNull()
                current?.let { cache.add(it) }
            } while (current != null)
        } catch (e: Throwable) {
            // Noop
        }
    }
}