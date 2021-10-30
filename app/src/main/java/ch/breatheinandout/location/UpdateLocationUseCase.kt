package ch.breatheinandout.location

import ch.breatheinandout.common.BaseObservable
import ch.breatheinandout.common.LocationHandler
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler
) : BaseObservable<UpdateLocationUseCase.Listener>(), LocationHandler.Listener {
    interface Listener {
        fun onSuccess(location: LocationPoint)
        fun onFailure()
    }

    fun update() {
        locationHandler.registerListener(this)
        locationHandler.getLastLocation()
    }

    override fun onUpdateLocationSuccess(location: LocationPoint) {
        notifySuccess(location)
    }

    override fun onUpdateLocationFailed(message: String, cause: Throwable) {
        notifyFailure(message)
    }

    private fun notifySuccess(location: LocationPoint) {
        getListeners().map { it.onSuccess(location) }
            .also { locationHandler.unregisterListener(this) }
    }

    private fun notifyFailure(msg: String) {
        getListeners().map { it.onFailure() }
            .also { locationHandler.unregisterListener(this) }
    }
}