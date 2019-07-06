package eventhandling

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

class Announcer<T : EventListener>(listenerType: Class<out T>) {
    private val proxy: T
    private val listeners = ArrayList<T>()

    init {
        proxy = listenerType.cast(Proxy.newProxyInstance(
                listenerType.classLoader,
                arrayOf<Class<*>>(listenerType)
        ) {
            _, method, args ->
            announce(method, args ?: emptyArray())
            null
        })
    }

    fun addListener(listener: T) {
        listeners.add(listener)
    }

    fun removeListener(listener: T) {
        listeners.remove(listener)
    }

    fun announce(): T {
        return proxy
    }

    private fun announce(m: Method, args: Array<Any>) {
        try {
            for (listener in listeners) {
                m.invoke(listener, *args)
            }
        } catch (e: IllegalAccessException) {
            throw IllegalArgumentException("could not invoke listener", e)
        } catch (e: InvocationTargetException) {
            when (val cause = e.cause) {
                is RuntimeException -> throw cause
                is Error -> throw cause
                else -> throw UnsupportedOperationException("listener threw exception", cause)
            }
        }
    }

    companion object {
        fun <T : EventListener> toListenerType(listenerType: Class<out T>): Announcer<T> {
            return Announcer(listenerType)
        }
    }
}
