package ch.breatheinandout.screen.airquality

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.breatheinandout.common.event.Event
import ch.breatheinandout.common.permissions.PermissionMember
import ch.breatheinandout.common.utils.FeatureAvailability
import ch.breatheinandout.domain.airquality.GetAirQualityDataUseCase
import ch.breatheinandout.domain.airquality.model.AirQuality
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import ch.breatheinandout.domain.location.UpdateLocationUseCase
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.nearbystation.GetNearbyStationUseCase
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val getNearbyStationUseCase: GetNearbyStationUseCase,
    private val getAirQualityDataUseCase: GetAirQualityDataUseCase
): ViewModel(), LifecycleObserver {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val locationPoint = MutableLiveData<LocationPoint>()
    private val nearbyStation = MutableLiveData<NearbyStation>()
    private val airQuality = MutableLiveData<AirQuality>()

    @Inject lateinit var featureAvailability: FeatureAvailability

    val viewState = MediatorLiveData<AirQualityViewState>()
    val viewEvent = MutableLiveData<Event<AirQualityEvent>>()

    init {
        Logger.v(" [INIT - AirQualityViewModel]")
        viewState.apply {
            addSource(locationPoint) {
                viewState.value = mergeMultipleSources(locationPoint, nearbyStation, airQuality)
                getNearbyStationList(it)
            }
            addSource(nearbyStation) {
                viewState.value = mergeMultipleSources(locationPoint, nearbyStation, airQuality)
                getAirQuality(it.stationName)
            }
            addSource(airQuality) {
                viewState.value = mergeMultipleSources(locationPoint, nearbyStation, airQuality)
            }
        }
    }


    private fun mergeMultipleSources(
        locationPointLiveData: MutableLiveData<LocationPoint>,
        nearbyStationLiveData: MutableLiveData<NearbyStation>,
        airQualityLiveData: MutableLiveData<AirQuality>
    ): AirQualityViewState {
        val locationPoint = locationPointLiveData.value
        val nearbyStation = nearbyStationLiveData.value
        val airQuality = airQualityLiveData.value

        // Do not notify content until each LiveData parameters get a value.
        if (locationPoint == null || nearbyStation == null || airQuality == null) {
            return Loading
        }

        return Content(locationPoint, nearbyStation, airQuality)
    }

    fun getLocation(address: SearchedAddress?) {
        coroutineScope.launch {
            val resultLocation: UpdateLocationUseCase.Result = updateLocationUseCase.update(address)
            handleResultLocation(resultLocation)
        }
    }

    private fun getNearbyStationList(location: LocationPoint) {
        coroutineScope.launch {
            val stationResult = getNearbyStationUseCase.getNearbyStation(location)
            handleResultNearbyStation(stationResult)
        }
    }

    private fun getAirQuality(stationName: String) {
        coroutineScope.launch {
            val airQualityResult = getAirQualityDataUseCase.get(stationName)
            handleResultAirQuality(airQualityResult)
        }
    }

    private fun handleResultAirQuality(result: GetAirQualityDataUseCase.Result) {
        when (result) {
            is GetAirQualityDataUseCase.Result.Success -> {
                airQuality.value = result.airQuality
                Logger.v("check(air) -> ${result.airQuality}")
            }
            is GetAirQualityDataUseCase.Result.Failure -> {
                Logger.e(result.message.plus("-> ${result.cause.message}"))
                viewState.value = Error
            }
        }
    }


    private fun handleResultNearbyStation(result: GetNearbyStationUseCase.Result) {
        when (result) {
            is GetNearbyStationUseCase.Result.Success -> {
                nearbyStation.value = result.nearbyStation
            }
            is GetNearbyStationUseCase.Result.Failure -> {
                Logger.e(result.message.plus("-> ${result.cause.message}"))
                viewState.value = Error
            }
        }
    }

    private fun handleResultLocation(result: UpdateLocationUseCase.Result) {
        when (result) {
            is UpdateLocationUseCase.Result.Success -> {
                locationPoint.value = result.location
            }
            is UpdateLocationUseCase.Result.Failure -> {
                Logger.v("Update Location failed -> ${result.message}")
                when (result.message) {
                    UpdateLocationUseCase.FAIL_ACTIVATE_GPS -> {
                        postEvent(ToastMessage("GPS 기능을 켜주세요."))
                    }
                    UpdateLocationUseCase.FAIL_ACTIVATE_NETWORK -> {
                        postEvent(ToastMessage("네트워크 연결상태를 확인해주세요."))
                    }
                    UpdateLocationUseCase.FAIL_LOCATION_HANDLER -> {
                        postEvent(ToastMessage("위치정보 권한허용이 필요합니다."))
                        postEvent(Permission(PermissionMember.FineLocation))
                    }
                }
                viewState.value = Error
            }
        }
    }

    private fun postEvent(event: AirQualityEvent) {
        viewEvent.value = Event(event)
    }


    // -----  Lifecycle things  -----
    override fun onCleared() {
        Logger.v(" [onCleared() - AirQualityViewModel]")
        super.onCleared()
    }
}