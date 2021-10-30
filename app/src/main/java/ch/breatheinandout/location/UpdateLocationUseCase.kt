package ch.breatheinandout.location

import ch.breatheinandout.common.BaseObservable
import ch.breatheinandout.common.LocationHandler
import ch.breatheinandout.location.data.Coordinates
import ch.breatheinandout.location.data.LocationPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler,
    private val findAddressLine: FindAddressLine
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

    override fun onUpdateLocationSuccess(coordinates: Coordinates) {
        coroutineScope.launch {
            findAddress(coordinates)
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

    private suspend fun findAddress(wgsCoordinates: Coordinates) {
        try {
            val addressLine = findAddressLine.findAddress(wgsCoordinates)
            notifySuccess(LocationPoint(addressLine, wgsCoordinates))
        } catch (exception: IOException) {
            // Failed while GeoCoder translate Coordinates to the address name.
            notifyFailure("GeoCoder failed.", exception)
        }
    }
}