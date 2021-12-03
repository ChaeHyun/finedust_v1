package ch.breatheinandout.screen.airquality

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import ch.breatheinandout.R
import ch.breatheinandout.common.Constants
import ch.breatheinandout.common.Constants.BACK_PRESSED_INTERVAL
import ch.breatheinandout.common.event.EventObserver
import ch.breatheinandout.common.utils.FeatureAvailability
import ch.breatheinandout.common.permissions.PermissionMember
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import ch.breatheinandout.screen.common.ScreenNavigator
import ch.breatheinandout.screen.airquality.widgetview.AirQualityWidgetView
import ch.breatheinandout.screen.common.widgetview.WidgetViewFactory
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

    private var backPressedTime : Long = 0
    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()
            val intervalTime = currentTime - backPressedTime

            if (intervalTime in 0..BACK_PRESSED_INTERVAL) {
                isEnabled = false
                activity?.onBackPressed()
            }
            else {
                backPressedTime = currentTime
                consumeEvent(ToastMessage(getString(R.string.message_for_back_pressed)))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        widgetView = widgetViewFactory.createAirQualityWidgetView(container)
        return widgetView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.v("onViewCreated()")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        widgetView.setupToolbarOptionsMenu()
        widgetView.registerListener(this)
        permissionRequester.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        permissionRequester.unregisterListener(this)
        widgetView.unregisterListener(this)
        widgetView.clearToolbarOptionsMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
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
            is ToastMessage -> {
                widgetView.showToastMessage(event.message)
            }
        }
    }

    override fun onShowDialogClicked() {
        screenNavigator.showDialog(R.id.AddressListDialog)
    }

    override fun onTriggerSwipeRefresh() {
        Logger.d("[Refresh] triggered")
        viewModel.getLocation(getAddressFromBundle(Constants.KEY_SELECTED_ADDRESS))
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
            // Start the main feature as soon as acquired the permission of FineLocation.
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