package ch.breatheinandout.screen.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.breatheinandout.domain.forecast.ForecastInfo
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_FORECAST_INFORM_CODE = "inform_code_forecast"
private const val ARG_FORECAST_INFO = "info_forecast"

@AndroidEntryPoint
class ForecastInfoPage : Fragment() {
    private var forecastCode: String? = null
    private var forecastInfo: ForecastInfo? = null

    @Inject lateinit var widgetViewFactory: WidgetViewFactory
    private lateinit var widgetView: ForecastPageWidgetView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            forecastCode = it.getString(ARG_FORECAST_INFORM_CODE)
            forecastInfo = it.getSerializable(ARG_FORECAST_INFO) as ForecastInfo?
        }
        Logger.v("CODE:$forecastCode, INFO: $forecastInfo")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        widgetView = widgetViewFactory.createForecastPageWidgetView(container)
        return widgetView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecastCode?.let { code -> widgetView.render(code, forecastInfo)}
    }

    companion object {
        @JvmStatic
        fun newInstance(code: String, info: ForecastInfo?) =
            ForecastInfoPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_FORECAST_INFORM_CODE, code)
                    putSerializable(ARG_FORECAST_INFO, info)
                }
            }
    }
}