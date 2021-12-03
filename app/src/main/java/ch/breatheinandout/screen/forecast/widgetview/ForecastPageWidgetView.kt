package ch.breatheinandout.screen.forecast.widgetview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ch.breatheinandout.domain.forecast.model.ForecastInfo
import ch.breatheinandout.screen.common.widgetview.BaseObservableWidgetView

abstract class ForecastPageWidgetView(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes val layoutId: Int
) : BaseObservableWidgetView<ForecastPageWidgetView.Listener>(layoutInflater, parent, layoutId) {

    interface Listener {
        // There is no action event for this page yet.
    }

    abstract fun render(code: String, data: ForecastInfo?)
}