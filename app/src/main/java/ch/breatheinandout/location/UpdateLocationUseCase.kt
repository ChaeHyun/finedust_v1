package ch.breatheinandout.location

import ch.breatheinandout.common.BaseObservable
import ch.breatheinandout.location.provider.FindAddressLine
import ch.breatheinandout.location.provider.LocationHandler
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.location.model.LocationPoint
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler,
    private val findAddressLine: FindAddressLine,
    private val transCoordinatesUseCase: TransCoordinatesUseCase
) : BaseObservable<UpdateLocationUseCase.Listener>(), LocationHandler.Listener {
    interface Listener {
        fun onSuccess(location: LocationPoint)
        fun onFailure(message: String, cause: Throwable)
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun update() {
        locationHandler.registerListener(this)
        locationHandler.getLastLocation()
    }

    override fun onUpdateLocationSuccess(wgsCoords: Coordinates) {
        coroutineScope.launch {
            try {
                val addressLine = findAddressLine.findAddress(wgsCoords)

                val transResult = handleTransCoordsResult(transCoordinatesUseCase.translateWgsToTmCoordinates(wgsCoords))

                transResult?.let { notifySuccess(LocationPoint(addressLine, transResult, wgsCoords)) }
                Logger.d("transResult -> $transResult")
            } catch (exception: IOException) {
                // Failed while GeoCoder translate Coordinates to the address name.
                notifyFailure("GeoCoder failed.", exception)
            }
        }
    }

    override fun onUpdateLocationFailed(message: String, cause: Throwable) {
        notifyFailure(message, cause)
    }

    private fun notifySuccess(location: LocationPoint) {
        getListeners().map { it.onSuccess(location) }
            .also { locationHandler.unregisterListener(this) }
    }

    private fun notifyFailure(msg: String, cause: Throwable) {
        getListeners().map { it.onFailure(msg, cause) }
            .also { locationHandler.unregisterListener(this) }
    }

    private fun handleTransCoordsResult(result: TransCoordinatesUseCase.Result) : Coordinates? {
        return when (result) {
            is TransCoordinatesUseCase.Result.Success -> {
                result.tmCoords
            }
            is TransCoordinatesUseCase.Result.Failure -> {
                notifyFailure("Failed to translate WGS coordinates to Tm coordinates.", NullPointerException())
                null
            }
        }
    }

}