package ch.breatheinandout.common

import ch.breatheinandout.location.LocationPoint
import ch.breatheinandout.location.LocationPointMapper
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import javax.inject.Inject

class LocationHandler @Inject constructor(
    private val providerClient: FusedLocationProviderClient,
    private val mapper: LocationPointMapper
) : BaseObservable<LocationHandler.Listener>() {
    interface Listener {
        fun onUpdateLocationSuccess(location: LocationPoint)
        fun onUpdateLocationFailed(message: String, cause: Throwable)
    }

    private var hasLocationCallback = false
    private val locationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            Logger.d("callback - onLocationResult()")
            notifyLocationSuccess(mapper.mapToDomainModel(result.lastLocation))
        }

        override fun onLocationAvailability(available: LocationAvailability) {
            super.onLocationAvailability(available)
            Logger.d(available.isLocationAvailable)
            // (isLocationAvailable == false) -> GPS feature is currently OFF.
        }
    }

    fun getLastLocation() {
        try {
            providerClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    notifyLocationSuccess(mapper.mapToDomainModel(location))
                } else {
                    startLocationUpdate(locationCallback)
                }
            }.addOnFailureListener {
                notifyLocationFailed(MSG_FAILED, NullPointerException())
            }
        } catch (exception: SecurityException) {
            notifyLocationFailed(MSG_PERMISSION_ERROR, exception)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun startLocationUpdate(locationCallback: LocationCallback) {
        Logger.v("[ startLocationUpdate() - isRunning ]")
        try {
            hasLocationCallback = true
            providerClient.requestLocationUpdates(
                provideLocationRequest(),
                locationCallback,
                null
            ).addOnFailureListener {
                notifyLocationFailed(MSG_FAILED, NullPointerException())
            }
        } catch (exception: SecurityException) {
            notifyLocationFailed(MSG_PERMISSION_ERROR, exception)
        }
    }

    private fun stopLocationUpdate(callback: LocationCallback) {
        if (hasLocationCallback) {
            providerClient.removeLocationUpdates(callback)
            hasLocationCallback = false
        }
    }

    private fun notifyLocationSuccess(location: LocationPoint) {
        getListeners().map { it.onUpdateLocationSuccess(location) }
            .also { stopLocationUpdate(locationCallback) }
    }

    private fun notifyLocationFailed(message: String, cause: Throwable) {
        getListeners().map { it.onUpdateLocationFailed(message, cause) }
            .also { stopLocationUpdate(locationCallback) }
    }

    /* The description using to request a LocationCallback. */
    private fun provideLocationRequest() : LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 30 * 1000
        fastestInterval = 5 * 1000
    }

    companion object {
        const val MSG_FAILED = "Failed to update a location."
        const val MSG_PERMISSION_ERROR = "Location Permission not allowed."
    }
}