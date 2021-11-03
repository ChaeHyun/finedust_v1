package ch.breatheinandout.screen.airquality

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ch.breatheinandout.screen.toolbar.ToolbarHelper
import ch.breatheinandout.screen.widgetview.BaseObservableWidgetView

abstract class AirQualityWidgetView(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : BaseObservableWidgetView<AirQualityWidgetView.Listener>(layoutInflater, parent, layoutId), ToolbarHelper {
    interface Listener {
        fun onClickButton()
        fun onTriggerSwipeRefresh()
    }

    abstract fun bindViewData(content: Content)
    abstract fun showProgressIndication()
    abstract fun hideProgressIndication()

    abstract fun showToastMessage(message: String)
    abstract fun render(viewState: AirQualityViewState)
}