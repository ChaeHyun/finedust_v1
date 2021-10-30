package ch.breatheinandout.screen.airquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ch.breatheinandout.common.FeatureAvailability
import ch.breatheinandout.common.permissions.PermissionMember
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AirQualityFragment : Fragment(), AirQualityWidgetView.Listener ,PermissionRequester.Listener {

    @Inject lateinit var widgetViewFactory: WidgetViewFactory
    @Inject lateinit var permissionRequester: PermissionRequester
    @Inject lateinit var featureAvailability: FeatureAvailability

    private val viewModel : AirQualityViewModel by viewModels()
    private lateinit var widgetView: AirQualityWidgetView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        widgetView = widgetViewFactory.createAirQualityWidgetView(container)
        // Inflate the layout for this fragment
        return widgetView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.v("onViewCreated()")
        widgetView.setToolbarVisibility(true)
        widgetView.setToolbarTitle("공기질")

        lifecycle.addObserver(viewModel)
    }

    override fun onStart() {
        super.onStart()
        permissionRequester.registerListener(this)
        widgetView.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        permissionRequester.unregisterListener(this)
        widgetView.unregisterListener(this)
    }

    // when the Test button clicked
    override fun onClickButton() {
        getLastLocation()
    }

    private fun getLastLocation() {
        if (permissionRequester.hasPermission(PermissionMember.FineLocation)) {
            if (!featureAvailability.isGpsFeatureOn()) {
                widgetView.showToastMessage("PLEASE TURN ON THE GPS.")
                return
            }
            viewModel.getLocation()
        } else {
            // Request Location Permission
            permissionRequester.requestPermission(PermissionMember.FineLocation, PermissionMember.REQUEST_CODE_PERMISSION)
        }
    }

    override fun onRequestPermissionResult(
        requestCode: Int,
        result: PermissionRequester.PermissionResult,
    ) {
        result.granted.map { Logger.v("# GRANTED: ${it.getAndroidPermission()}") }
        result.denied.map { Logger.v("# DENIED: ${it.getAndroidPermission()}") }
        result.doNotAsk.map { Logger.v("# DoNotAsk: ${it.getAndroidPermission()}") }
    }

    override fun onPermissionRequestCancelled(requestCode: Int) {
        Logger.d(" Permission Cancelled -> requestCode: $requestCode")
    }
}