package ch.breatheinandout.screen.airquality

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import ch.breatheinandout.location.LocationPoint
import ch.breatheinandout.location.UpdateLocationUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val locationUseCase: UpdateLocationUseCase
): ViewModel(), LifecycleObserver, UpdateLocationUseCase.Listener {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        Logger.v(" [INIT - AirQualityViewModel]")
    }

    fun getLocation() {
        // TODO: 권한체크, GPS TurnOn check
        locationUseCase.update()
    }

    // ---- Callback from UpdateLocationUseCase -----
    override fun onSuccess(location: LocationPoint) {
        Logger.v(" [updateLocation] -> check: ${location.longitudeX}, ${location.latitudeY}")
    }

    override fun onFailure() {
        Logger.v(" [updateLocationFailed - ]")
    }


    // -----  Lifecycle things  -----
    override fun onCleared() {
        Logger.v(" [onCleared() - AirQualityViewModel]")
        super.onCleared()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun airFragmentOnStarted() {
        Logger.v(" [fragment.onStart() - AirQualityViewModel]")
        locationUseCase.registerListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun airFragmentOnStopped() {
        Logger.v(" [fragment.onStop() - AirQualityViewModel]")
        locationUseCase.unregisterListener(this)
    }
}