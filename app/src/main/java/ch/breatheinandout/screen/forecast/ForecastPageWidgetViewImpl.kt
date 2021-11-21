package ch.breatheinandout.screen.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ch.breatheinandout.R
import ch.breatheinandout.domain.forecast.ForecastInfo
import ch.breatheinandout.domain.forecast.O3
import ch.breatheinandout.screen.imageloader.ImageLoader
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import com.orhanobut.logger.Logger
import hilt_aggregated_deps._ch_breatheinandout_screen_forecast_ForecastFragment_GeneratedInjector

class ForecastPageWidgetViewImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    val navDrawerHelper: NavDrawerHelper,
    val imageLoader: ImageLoader
) : ForecastPageWidgetView(inflater, parent, R.layout.layout_forecast) {

    private val dataTime: TextView = findViewById(R.id.forecast_date)
    private val todayOverall: TextView = findViewById(R.id.text_content_today)
    private val tomorrowOverall: TextView = findViewById(R.id.text_content_tomorrow)
    private val todayCauses: TextView = findViewById(R.id.text_content_causes)
    private val todayGrades: TextView = findViewById(R.id.text_content_grade)

    private val yeboImageLayout: LinearLayout = findViewById(R.id.imageLayout)
    private val yeboGifImage: ImageView = findViewById(R.id.img_yebo)

    override fun render(code: String, data: ForecastInfo?) {
        if (data != null) {
            dataTime.text = data.dataTime
            todayOverall.text = data.informOverallToday
            tomorrowOverall.text = data.informOverallTomorrow
            todayCauses.text = data.informCause
            todayGrades.text = data.informGrade
            loadImage(data.imageUrl)
            yeboImageLayout.visibility = View.VISIBLE
        } else {
            if (code == O3.code)
                todayOverall.text = getString(R.string.forecast_notification_o3)
        }
    }

    private fun loadImage(imageUrl: String) {
        imageLoader.loadImage(imageUrl, yeboGifImage)
    }
}