package ch.breatheinandout.screen.airquality.widgetview

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.breatheinandout.R
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

    private val date: TextView = findViewById(R.id.text_date)
    private val textLocation: TextView = findViewById(R.id.text_content_location)
    private val textStation: TextView = findViewById(R.id.text_content_station)

    init {
        swipeRefresh.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                Thread.sleep(2000)      // Pretend there is a few delay to see the progress icon.
                swipeRefresh.isRefreshing = false
            }
            getListeners().forEach { it.onTriggerSwipeRefresh() }
        }

        clickableLayout.setOnClickListener {
            getListeners().forEach { it.onClickButton() }
        }
    }

    override fun render(viewState: AirQualityViewState) {
        when (viewState) {
            Loading -> { showProgressIndication() }
            Error -> {
                Logger.e("render view state -> Error")
                hideProgressIndication()
            }
            Refresh -> TODO()
            is Content -> {
                bindViewData(viewState)
            }
        }
    }

    override fun bindViewData(content: Content) {
        content.location.addressLine.also { textLocation.text = it.addr }
        val nearbyStation = content.nearbyStation
        val stationString = "${nearbyStation.stationName} 측정소까지 ${nearbyStation.distance}km\n${nearbyStation.stationAddressLine}"
        textStation.text = stationString

        hideProgressIndication()
    }

    override fun showProgressIndication() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressIndication() {
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

    override fun showToastMessage(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }
}