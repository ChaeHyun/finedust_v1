package ch.breatheinandout.screen.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import ch.breatheinandout.screen.airquality.AirQualityWidgetView
import ch.breatheinandout.screen.airquality.AirQualityWidgetViewImpl
import ch.breatheinandout.screen.toolbar.ToolbarWidgetView
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetViewImpl
import ch.breatheinandout.screen.searchaddress.SearchAddressWidgetView
import ch.breatheinandout.screen.searchaddress.SearchAddressWidgetViewImpl
import javax.inject.Inject

/* It creates WidgetView Instances and provide them. */
class WidgetViewFactory @Inject constructor(
    private val layoutInflater: LayoutInflater,
    private val navDrawerHelper: NavDrawerHelper
) {

    /** This WidgetView contains NavigationDrawer features in it. */
    fun createNavDrawerWidgetView(parent: ViewGroup?) : NavDrawerWidgetView {
        return NavDrawerWidgetViewImpl(layoutInflater, parent, this)
    }

    fun getNavDrawerHelper(): NavDrawerHelper = navDrawerHelper

    fun getToolbarWidgetView(parent: ViewGroup?) : ToolbarWidgetView = ToolbarWidgetView(layoutInflater, parent)
    fun createAirQualityWidgetView(parent: ViewGroup?): AirQualityWidgetView = AirQualityWidgetViewImpl(layoutInflater, parent, navDrawerHelper)
    fun createSearchAddressWidgetView(parent: ViewGroup?): SearchAddressWidgetView = SearchAddressWidgetViewImpl(layoutInflater, parent, navDrawerHelper)

}