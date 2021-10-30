package ch.breatheinandout.screen.airquality

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import ch.breatheinandout.R
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper

class AirQualityWidgetViewImpl constructor(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup?,
    private val navDrawerHelper: NavDrawerHelper    // in order to propagate orders to NavDrawer
) : AirQualityWidgetView(inflater, parent, R.layout.fragment_airquality) {

    private val button: Button = findViewById(R.id.button_location)

    init {
        button.setOnClickListener {
            getListeners().forEach { it.onClickButton() }
        }
    }

    override fun showProgressIndication() {
        TODO("Not yet implemented")
    }

    override fun hideProgressIndication() {
        TODO("Not yet implemented")
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