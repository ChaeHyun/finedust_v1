package ch.breatheinandout.screen.airquality

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import ch.breatheinandout.location.UpdateLocationUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val locationUseCase: UpdateLocationUseCase
): ViewModel(), LifecycleObserver {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        Logger.v(" [INIT - AirQualityViewModel]")
    }

    fun getLocation() {
        coroutineScope.launch {
            val result = locationUseCase.update()
            printResult(result)
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