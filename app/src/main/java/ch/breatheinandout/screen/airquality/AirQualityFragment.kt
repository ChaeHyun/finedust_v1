package ch.breatheinandout.screen.airquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ch.breatheinandout.R
import ch.breatheinandout.common.Constants
import ch.breatheinandout.common.event.EventObserver
import ch.breatheinandout.common.utils.FeatureAvailability
import ch.breatheinandout.common.permissions.PermissionMember
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import ch.breatheinandout.screen.ScreenNavigator
import ch.breatheinandout.screen.airquality.widgetview.AirQualityWidgetView
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AirQualityFragment : Fragment(), AirQualityWidgetView.Listener ,PermissionRequester.Listener {

    @Inject lateinit var widgetViewFactory: WidgetViewFactory
    @Inject lateinit var permissionRequester: PermissionRequester
    @Inject lateinit var featureAvailability: FeatureAvailability
    @Inject lateinit var screenNavigator: ScreenNavigator

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
        lifecycle.addObserver(viewModel)

        viewModel.viewState.observe(viewLifecycleOwner, { state -> render(state) })
        viewModel.viewEvent.observe(viewLifecycleOwner, EventObserver { consumeEvent(it) })

        val argAddress: SearchedAddress? = getAddressFromBundle(Constants.KEY_SELECTED_ADDRESS)
        Logger.i("check(SearchedAddress) -> $argAddress")
        viewModel.getLocation(argAddress)
    }

    private fun getAddressFromBundle(key: String): SearchedAddress? {
        if (arguments != null && requireArguments().containsKey(key)) {
            return requireArguments().getSerializable(key) as SearchedAddress
        }
        return null
    }

    override fun onStart() {
        super.onStart()
        widgetView.setToolbarVisibility(true)
        widgetView.registerListener(this)
        permissionRequester.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        permissionRequester.unregisterListener(this)
        widgetView.unregisterListener(this)
        widgetView.resetToolbarColor()
    }

    private fun render(viewState: AirQualityViewState) {
        when (viewState) {
            Loading -> { widgetView.showProgressIndication() }
            Error -> {
                Logger.e("render view state -> Error")
                widgetView.hideProgressIndication()
            }
            Refresh -> { }
            is Content -> {
                widgetView.bindViewData(viewState)
            }
        }
    }

    private fun consumeEvent(event: AirQualityEvent) {
        when (event) {
            is Permission -> {
                requestPermission(event.permission)
            }
            is Toast -> {
                widgetView.showToastMessage(event.message)
            }
        }
    }

    // when the Test button clicked
    override fun onClickButton() {
        screenNavigator.showDialog(R.id.AddressListDialog)
    }

    override fun onTriggerSwipeRefresh() {
        Logger.d("[Refresh] triggered")
        viewModel.getLocation(getAddressFromBundle(Constants.KEY_SELECTED_ADDRESS))
    }


    // TODO: checking GPS is ON or OFF.
    private fun getLastLocation(address: SearchedAddress?) {
        if (permissionRequester.hasPermission(PermissionMember.FineLocation)) {
            if (!featureAvailability.isGpsFeatureOn()) {
                widgetView.showToastMessage("PLEASE TURN ON THE GPS.")
                return
            }
            viewModel.getLocation(address)
        } else {
            // Request Location Permission
            permissionRequester.requestPermission(PermissionMember.FineLocation, PermissionMember.REQUEST_CODE_PERMISSION)
        }
    }
    private fun requestPermission(permission: PermissionMember) {
        if (!permissionRequester.hasPermission(permission)) {
            permissionRequester.requestPermission(permission, PermissionMember.REQUEST_CODE_PERMISSION)
        }
    }

    override fun onRequestPermissionResult(
        requestCode: Int,
        result: PermissionRequester.PermissionResult,
    ) {
        result.granted.map { Logger.v("# GRANTED: ${it.getAndroidPermission()}") }
        result.denied.map { Logger.v("# DENIED: ${it.getAndroidPermission()}") }
        result.doNotAsk.map { Logger.v("# DoNotAsk: ${it.getAndroidPermission()}") }

        if (result.granted.contains(PermissionMember.FineLocation)) {
            Logger.d("권한이 허용 됐을 때, 바로 시작하기.")
            viewModel.getLocation(null)
        }
        if (result.denied.contains(PermissionMember.FineLocation)) {
            widgetView.showToastMessage("권한 허용을 하지 않으면 기능이 작동하지 않을 수 있습니다.")
        }
    }

    override fun onPermissionRequestCancelled(requestCode: Int) {
        Logger.d(" Permission Cancelled -> requestCode: $requestCode")
        widgetView.showToastMessage("권한 허용을 하지 않으면 기능이 작동하지 않을 수 있습니다.")
    }
}