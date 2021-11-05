package ch.breatheinandout.screen.airquality

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import ch.breatheinandout.domain.location.UpdateLocationUseCase
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.nearbystation.GetNearbyStationUseCase
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
    private val getNearbyStationUseCase: GetNearbyStationUseCase
): ViewModel(), LifecycleObserver {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val locationPoint = MutableLiveData<LocationPoint>()
    private val nearbyStation = MutableLiveData<NearbyStation>()

    val viewState = MediatorLiveData<AirQualityViewState>()

    init {
        Logger.v(" [INIT - AirQualityViewModel]")
        viewState.apply {
            addSource(locationPoint) {
                viewState.value = mergeMultipleSources(locationPoint, nearbyStation)
                getNearbyStationList(it)
            }
            addSource(nearbyStation) {
                viewState.value = mergeMultipleSources(locationPoint, nearbyStation)
            }
        }
    }

    private fun mergeMultipleSources(
        locationPointLiveData: MutableLiveData<LocationPoint>,
        nearbyStationLiveData: MutableLiveData<NearbyStation>,
    ): AirQualityViewState {
        val locationPoint = locationPointLiveData.value
        val nearbyStation = nearbyStationLiveData.value

        // Do not notify content until each LiveData parameters get a value.
        if (locationPoint == null || nearbyStation == null) {
            return Loading
        }

        return Content(locationPoint, nearbyStation)
    }

    fun getLocation() {
        coroutineScope.launch {
            val resultLocation: UpdateLocationUseCase.Result = updateLocationUseCase.update()
            handleResultLocation(resultLocation)
        }
    }

    private fun getNearbyStationList(location: LocationPoint) {
        coroutineScope.launch {
            val stationResult = getNearbyStationUseCase.getNearbyStation(location)
            handleResultNearbyStation(stationResult)
        }
    }

    private fun handleResultNearbyStation(result: GetNearbyStationUseCase.Result) {
        when (result) {
            is GetNearbyStationUseCase.Result.Success -> {
                nearbyStation.value = result.nearbyStation
//                Logger.d("check(nearby) -> ${result.nearbyStation}")
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
                // Printing values at log.
//                val location = result.location
//                Logger.v(" [updateLocation] -> check: ${location.wgsCoords?.longitudeX}, ${location.wgsCoords?.latitudeY}" +
//                        "\n [findAddressLine] -> check: ${location.addressLine.addr}, ${location.addressLine.umdName}" +
//                        "\n [transCoords] -> check: ${location.tmCoords.longitudeX}, ${location.tmCoords.latitudeY}"
//                )
                locationPoint.value = result.location
            }
            is UpdateLocationUseCase.Result.Failure -> {
                Logger.v("Update Location failed -> ${result.message}")
                viewState.value = Error
            }
        }
    }


    // -----  Lifecycle things  -----
    override fun onCleared() {
        Logger.v(" [onCleared() - AirQualityViewModel]")
        super.onCleared()
    }
}