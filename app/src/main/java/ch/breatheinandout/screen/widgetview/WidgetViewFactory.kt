package ch.breatheinandout.screen.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import ch.breatheinandout.screen.toolbar.ToolbarWidgetView
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetViewImpl
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

}