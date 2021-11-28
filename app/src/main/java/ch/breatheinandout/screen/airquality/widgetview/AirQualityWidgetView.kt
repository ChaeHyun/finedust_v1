package ch.breatheinandout.screen.airquality.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ch.breatheinandout.screen.airquality.AirQualityViewState
import ch.breatheinandout.screen.airquality.Content
import ch.breatheinandout.screen.toolbar.ToolbarHelper
import ch.breatheinandout.screen.widgetview.BaseObservableWidgetView

abstract class AirQualityWidgetView(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : BaseObservableWidgetView<AirQualityWidgetView.Listener>(layoutInflater, parent, layoutId), ToolbarHelper {
    interface Listener {
        fun onShowDialogClicked()
        fun onTriggerSwipeRefresh()
    }

    abstract fun showProgressIndication()
    abstract fun hideProgressIndication()

    abstract fun bindViewData(content: Content)
    abstract fun showToastMessage(message: String)

    abstract fun resetToolbarColor()
    abstract fun setupToolbarOptionsMenu()
    abstract fun clearToolbarOptionsMenu()

    abstract fun isDrawerOpen(): Boolean
    abstract fun closeDrawer()
}