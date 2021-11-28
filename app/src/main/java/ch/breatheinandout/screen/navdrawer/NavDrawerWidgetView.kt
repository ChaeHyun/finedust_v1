package ch.breatheinandout.screen.navdrawer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import ch.breatheinandout.R
import ch.breatheinandout.screen.toolbar.ToolbarHelper
import ch.breatheinandout.screen.widgetview.BaseObservableWidgetView
import com.google.android.material.navigation.NavigationView

abstract class NavDrawerWidgetView(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : BaseObservableWidgetView<NavDrawerWidgetView.Listener>(layoutInflater, parent, layoutId),
    ToolbarHelper {
    interface Listener {
        // Actions for NavDrawer
        fun onNavItemClicked(item: DrawerItem)
    }

    sealed class DrawerItem(val resId: Int) {
        object Home : DrawerItem(R.id.AirQualityFragment)
        object Search : DrawerItem(R.id.SearchAddressFragment)
        object SelectAddress : DrawerItem(R.id.AddressListDialog)
        object Forecast : DrawerItem(R.id.ForecastFragment)
        object Informative : DrawerItem(R.id.InformativeFragment)
        object Setting : DrawerItem(R.id.SettingsFragment)
        object AppInfoDialog : DrawerItem(R.id.AppInfoDialog)
    }

    abstract fun isDrawerOpen(): Boolean

    abstract fun openDrawer()
    abstract fun closeDrawer()

    abstract fun getNavigationView(): NavigationView
    abstract fun getDrawerLayout(): DrawerLayout
    abstract fun getFrameLayout(): FrameLayout

    abstract fun getToolbarWidgetView(): Toolbar

    abstract fun setupToolbarOptionsMenu()
    abstract fun clearToolbarOptionsMenu()

}

