package ch.breatheinandout.screen.airquality

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.breatheinandout.nearbystation.model.NearbyStation
import ch.breatheinandout.location.UpdateLocationUseCase
import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.nearbystation.GetNearbyStationUseCase
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


    init {
        Logger.v(" [INIT - AirQualityViewModel]")
    }

    fun getLocation() {
        coroutineScope.launch {
            val resultLocation = updateLocationUseCase.update()
            printResult(resultLocation)
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
                // nearbyStationLiveData <- result
                nearbyStation.value = result.nearbyStation
                Logger.d("check(nearby) -> ${result.nearbyStation}")
            }
            is GetNearbyStationUseCase.Result.Failure -> {
                Logger.e(result.message.plus("-> ${result.cause.message}"))
                // Network Failed
            }
        }
    }


    private fun printResult(result: UpdateLocationUseCase.Result) {
        when (result) {
            is UpdateLocationUseCase.Result.Success -> {
                val location = result.location
                Logger.v(" [updateLocation] -> check: ${location.wgsCoords?.longitudeX}, ${location.wgsCoords?.latitudeY}" +
                        "\n [findAddressLine] -> check: ${location.addressLine.addr}, ${location.addressLine.umdName}" +
                        "\n [transCoords] -> check: ${location.tmCoords.longitudeX}, ${location.tmCoords.latitudeY}"
                )
                locationPoint.value = result.location
                getNearbyStationList(result.location)
            }
            is UpdateLocationUseCase.Result.Failure -> {
                Logger.v("Update Location failed -> ${result.message}")
            }
        }
    }


    // -----  Lifecycle things  -----
    override fun onCleared() {
        Logger.v(" [onCleared() - AirQualityViewModel]")
        super.onCleared()
    }
}