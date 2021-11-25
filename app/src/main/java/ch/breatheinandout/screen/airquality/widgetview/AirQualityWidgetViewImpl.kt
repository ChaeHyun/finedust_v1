package ch.breatheinandout.screen.airquality.widgetview

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.breatheinandout.R
import ch.breatheinandout.domain.airquality.model.*
import ch.breatheinandout.screen.airquality.*
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AirQualityWidgetViewImpl constructor(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup?,
    private val navDrawerHelper: NavDrawerHelper    // in order to propagate orders to NavDrawer
) : AirQualityWidgetView(inflater, parent, R.layout.fragment_airquality) {

    private val swipeRefresh: SwipeRefreshLayout = findViewById(R.id.swipeRefresh)
    private val clickableLayout: LinearLayout = findViewById(R.id.layout_click_addr)
    private val layoutContent: RelativeLayout = findViewById(R.id.layout_content)

    private val date: TextView = findViewById(R.id.text_date)
    private val textLocation: TextView = findViewById(R.id.text_content_location)
    private val textStation: TextView = findViewById(R.id.text_content_station)

    private data class AirQualityFactor(val img: ImageView, val value: TextView)
    private val khai: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_khai), findViewById(R.id.text_value_khai))
    private val pm10: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_pm10), findViewById(R.id.text_value_pm10))
    private val pm25: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_pm25), findViewById(R.id.text_value_pm25))
    private val o3: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_o3), findViewById(R.id.text_value_o3))
    private val no2: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_no2), findViewById(R.id.text_value_no2))
    private val co: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_co), findViewById(R.id.text_value_co))
    private val so2: AirQualityFactor = AirQualityFactor(findViewById(R.id.img_so2), findViewById(R.id.text_value_so2))
    private val airQualityFactors = listOf(pm10, pm25, o3, co, no2, so2)

    init {
        swipeRefresh.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                Thread.sleep(1500)      // Pretend there is a few delay to see the progress icon.
                swipeRefresh.isRefreshing = false
            }
            getListeners().forEach { it.onTriggerSwipeRefresh() }
        }

        clickableLayout.setOnClickListener {
            getListeners().forEach { it.onClickButton() }
        }
    }

    override fun bindViewData(content: Content) {
        content.location.addressLine.also { textLocation.text = it.addr }
        val nearbyStation = content.nearbyStation
        val stationString = "${nearbyStation.stationName} 측정소까지 ${nearbyStation.distance}km\n${nearbyStation.stationAddressLine}"
        textStation.text = stationString

        val airQuality = content.airQuality
        date.text = airQuality.dataTime.substring(0,13).plus("시 발표")
        setTextColorAndImageResource(khai, KHAI, airQuality.khaiGrade, content.airQuality.khaiValue)
        for (key in airQuality.detail.keys) {
            val index = key.order
            when (key) {
                PM10, PM25 -> {
                    setTextColorAndImageResource(airQualityFactors[index], PM, airQuality.detail[key]?.grade1h, airQuality.detail[key]?.value)
                }
                O3, CO, SO2, NO2 -> {
                    setTextColorAndImageResource(airQualityFactors[index], PM, airQuality.detail[key]?.grade, airQuality.detail[key]?.value)
                }
            }
        }

        hideProgressIndication()
    }

    override fun showProgressIndication() {
        layoutContent.visibility = View.GONE
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressIndication() {
        layoutContent.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = false
    }

    override fun setToolbarTitle(title: String) {
        navDrawerHelper.setToolbarTitle(title)
    }

    override fun setToolbarBackgroundColor(color: ColorDrawable) {
        navDrawerHelper.setToolbarBackgroundColor(color)
    }

    override fun setToolbarVisibility(visible: Boolean) {
        navDrawerHelper.setToolbarVisibility(visible)
    }

    override fun resetToolbarColor() {
        setToolbarBackgroundColor(ColorDrawable(DefaultColor.defaultColor[0]))
        navDrawerHelper.applyStatusBarColor(DefaultColor.defaultColor[1])
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setTextColorAndImageResource(factor: AirQualityFactor, type: ImgType, gradeStr: String?, valueStr: String?) {
        val grade = parseGradeStr(gradeStr)
        factor.value.setTextColor(TextColor.textColor[grade])
        factor.value.text = valueStr
        setGradeImageResource(factor.img, type, grade)

        if (factor.value.id == R.id.text_value_khai)
            changeToolbarColor(grade)
    }

    private fun setGradeImageResource(img: ImageView, type: ImgType, grade: Int) {
        val drawableId = GradeResources.resourceArray[type.order][grade]
        img.setImageDrawable(ResourcesCompat.getDrawable(getContext().resources, drawableId, null))
    }

    private fun parseGradeStr(grade: String?): Int {
        return when (grade) {
            null -> 0
            "", " ", "-" -> 0
            else -> Integer.parseInt(grade)         // parameter 가 "-" 일 때 runtime error
        }
    }

    private fun changeToolbarColor(index: Int) {
        setToolbarBackgroundColor(ColorDrawable(ToolbarColor.toolbarColor[index]))
        navDrawerHelper.applyStatusBarColor(StatusBarColor.statusBarColor[index])
    }


}