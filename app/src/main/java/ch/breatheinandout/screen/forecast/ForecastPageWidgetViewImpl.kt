package ch.breatheinandout.screen.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import ch.breatheinandout.R
import ch.breatheinandout.domain.forecast.Forecast
import ch.breatheinandout.domain.forecast.ForecastInfo
import ch.breatheinandout.domain.forecast.O3
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper

class ForecastPageWidgetViewImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    val navDrawerHelper: NavDrawerHelper
) : ForecastPageWidgetView(inflater, parent, R.layout.layout_forecast) {

    private val dataTime: TextView = findViewById(R.id.forecast_date)
    private val todayOverall: TextView = findViewById(R.id.text_content_today)
    private val tomorrowOverall: TextView = findViewById(R.id.text_content_tomorrow)
    private val todayCauses: TextView = findViewById(R.id.text_content_causes)
    private val todayGrades: TextView = findViewById(R.id.text_content_grade)

    override fun render(code: String, data: ForecastInfo?) {
        if (data != null) {
            dataTime.text = data.dataTime
            todayOverall.text = data.informOverallToday
            tomorrowOverall.text = data.informOverallTomorrow
            todayCauses.text = data.informCause
            todayGrades.text = data.informGrade
        } else {
            if (code == O3.code)
                todayOverall.text = getString(R.string.forecast_notification_o3)
        }
    }
}