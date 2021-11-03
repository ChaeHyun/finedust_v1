package ch.breatheinandout.common

import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class BaseObservable<LISTENER> {
    private val listeners: MutableSet<LISTENER> = Collections.newSetFromMap(
        ConcurrentHashMap(1)
    )

    fun registerListener(listener: LISTENER) = listeners.add(listener)

    fun unregisterListener(listener: LISTENER) = listeners.remove(listener)

    protected fun getListeners() : Set<LISTENER> = Collections.unmodifiableSet(listeners)
}