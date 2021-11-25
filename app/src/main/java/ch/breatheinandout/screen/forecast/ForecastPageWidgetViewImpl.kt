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
    private val todayGrades: List<TextView> = mutableListOf(
        findViewById(R.id.text_content_grade0),
        findViewById(R.id.text_content_grade1),
        findViewById(R.id.text_content_grade2),
        findViewById(R.id.text_content_grade3),
        findViewById(R.id.text_content_grade4),
        findViewById(R.id.text_content_grade5),
        findViewById(R.id.text_content_grade6),
        findViewById(R.id.text_content_grade7),
        findViewById(R.id.text_content_grade8),

    )

    private val yeboImageLayout: LinearLayout = findViewById(R.id.imageLayout)
    private val yeboGifImage: ImageView = findViewById(R.id.img_yebo)

    override fun render(code: String, data: ForecastInfo?) {
        try {
            if (data != null) {
                val gradeList = prettyStringForGrade(data.grades)

                val prettyDate = data.dataTime.substring(0..12).plus("시 발표")
                dataTime.text = prettyDate
                todayOverall.text = data.informOverallToday
                tomorrowOverall.text = data.informOverallTomorrow
                todayCauses.text = data.informCause
                loadImage(data.imageUrl)
                for (index in todayGrades.indices) {
                    todayGrades[index].text = gradeList[index]
                }
            } else {
                if (code == O3.code)
                    todayOverall.text = getString(R.string.forecast_notification_o3)
            }
        } catch (e: Exception) {
            Logger.e("Error rendering the view.")
        }
    }

    private fun loadImage(imageUrl: String) {
        imageLoader.loadImage(imageUrl, yeboGifImage)
        yeboImageLayout.visibility = View.VISIBLE
    }

    private fun prettyStringForGrade(gradePairs: Map<String, String>) : List<String> {
        val divider: List<Int> = listOf(1, 3, 5, 7, 9, 12, 14, 17)
        val array: Array<String> = Array(9) {""}
        gradePairs.onEachIndexed { mapIndex, entry ->
            val arrayIndex = hitRange(mapIndex)
            array[arrayIndex] += "[${entry.key}: ${entry.value}]"

            if (!divider.contains(mapIndex) && mapIndex != (gradePairs.size-1)) {
                array[arrayIndex] += " , "
            }
        }

        return array.asList()
    }

    private fun hitRange(mapIndex: Int) : Int {
        val divider: List<Int> = listOf(1, 3, 5, 7, 9, 12, 14, 17)
        return when {
            mapIndex <= divider[0] -> 0
            mapIndex <= divider[1] -> 1
            mapIndex <= divider[2] -> 2
            mapIndex <= divider[3] -> 3
            mapIndex <= divider[4] -> 4
            mapIndex <= divider[5] -> 5
            mapIndex <= divider[6] -> 6
            mapIndex <= divider[7] -> 7
            else -> 8
        }
    }
}