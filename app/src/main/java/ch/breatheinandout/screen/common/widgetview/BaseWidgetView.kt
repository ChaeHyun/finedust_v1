package ch.breatheinandout.screen.common.widgetview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

abstract class BaseWidgetView(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int,
    private val attachToRoot: Boolean = false
) : WidgetView {

    private val rootView: View = layoutInflater.inflate(layoutId, parent, attachToRoot)

    override fun getRootView(): View {
        return rootView
    }

    protected fun <T: View?> findViewById(@IdRes id: Int): T {
        return rootView.findViewById<T>(id)
    }

    protected fun getContext(): Context = getRootView().context
    protected fun getString(@StringRes resId: Int) = getContext().getString(resId)
}