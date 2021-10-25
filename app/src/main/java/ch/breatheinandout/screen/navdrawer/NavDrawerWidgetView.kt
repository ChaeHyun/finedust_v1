package ch.breatheinandout.screen.navdrawer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.drawerlayout.widget.DrawerLayout
import ch.breatheinandout.screen.widgetview.BaseObservableWidgetView
import com.google.android.material.navigation.NavigationView

abstract class NavDrawerWidgetView(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : BaseObservableWidgetView<NavDrawerWidgetView.Listener>(layoutInflater, parent, layoutId) {
    interface Listener {
        // Actions for NavDrawer
        fun onNavItemClicked(item: DrawerItem)
    }

    sealed class DrawerItem {
        object Home : DrawerItem()
        object Search : DrawerItem()
        object Setting : DrawerItem()
    }

    abstract fun isDrawerOpen(): Boolean

    abstract fun openDrawer()
    abstract fun closeDrawer()

    abstract fun getNavigationView(): NavigationView
    abstract fun getDrawerLayout(): DrawerLayout
    abstract fun getFrameLayout(): FrameLayout

}