package ch.breatheinandout.screen.common.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.util.*
import kotlin.collections.HashSet

abstract class BaseObservableWidgetView<LISTENER>(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : BaseWidgetView(layoutInflater, parent, layoutId), ObservableWidgetView<LISTENER> {

    private val listeners: MutableSet<LISTENER> = HashSet()

    override fun registerListener(listener: LISTENER) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: LISTENER) {
        listeners.remove(listener)
    }

    protected fun getListeners(): Set<LISTENER> = Collections.unmodifiableSet(listeners)
}