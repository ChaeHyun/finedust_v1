package ch.breatheinandout.screen.common.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import ch.breatheinandout.screen.airquality.widgetview.AirQualityWidgetView
import ch.breatheinandout.screen.airquality.widgetview.AirQualityWidgetViewImpl
import ch.breatheinandout.screen.forecast.widgetview.ForecastPageWidgetView
import ch.breatheinandout.screen.forecast.widgetview.ForecastPageWidgetViewImpl
import ch.breatheinandout.screen.common.imageloader.ImageLoader
import ch.breatheinandout.screen.common.toolbar.ToolbarWidgetView
import ch.breatheinandout.screen.common.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.common.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.common.navdrawer.NavDrawerWidgetViewImpl
import ch.breatheinandout.screen.searchaddress.widgetview.SearchAddressWidgetView
import ch.breatheinandout.screen.searchaddress.widgetview.SearchAddressWidgetViewImpl
import javax.inject.Inject

/* It creates WidgetView Instances and provide them. */
class WidgetViewFactory @Inject constructor(
    private val layoutInflater: LayoutInflater,
    private val navDrawerHelper: NavDrawerHelper,
    private val imageLoader: ImageLoader
) {

    /** This WidgetView contains NavigationDrawer features in it. */
    fun createNavDrawerWidgetView(parent: ViewGroup?) : NavDrawerWidgetView {
        return NavDrawerWidgetViewImpl(layoutInflater, parent, this)
    }

    fun getNavDrawerHelper(): NavDrawerHelper = navDrawerHelper

    fun getToolbarWidgetView(parent: ViewGroup?) : ToolbarWidgetView = ToolbarWidgetView(layoutInflater, parent)
    fun createAirQualityWidgetView(parent: ViewGroup?): AirQualityWidgetView = AirQualityWidgetViewImpl(layoutInflater, parent, navDrawerHelper)
    fun createSearchAddressWidgetView(parent: ViewGroup?): SearchAddressWidgetView = SearchAddressWidgetViewImpl(layoutInflater, parent, navDrawerHelper)
    fun createForecastPageWidgetView(parent: ViewGroup?): ForecastPageWidgetView = ForecastPageWidgetViewImpl(layoutInflater, parent, navDrawerHelper, imageLoader)

}