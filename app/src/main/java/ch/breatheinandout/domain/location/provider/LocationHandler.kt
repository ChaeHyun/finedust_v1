package ch.breatheinandout.domain.location.provider

import android.os.Looper
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.domain.location.model.coordinates.CoordinatesMapper
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import javax.inject.Inject


class LocationHandler @Inject constructor(
    private val providerClient: FusedLocationProviderClient,
    private val mapper: CoordinatesMapper
) {
    private var hasLocationCallback = false

    suspend fun getLocationDeferred(): Coordinates? {
        val deferred = CompletableDeferred<LocationResult?>()
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                deferred.complete(location)
                Logger.v(">> onLocationResult -> set deferred.complete(location)")
            }

            override fun onLocationAvailability(avail: LocationAvailability) {
                super.onLocationAvailability(avail)
                if (!avail.isLocationAvailable) {
                    deferred.complete(null)
                    Logger.v(">> onLocationAvailability is false -> set deferred.complete(null)")
                }
            }
        }

        // Request Location updates.
        return try {
            providerClient.requestLocationUpdates(
                provideLocationRequest(), locationCallback, Looper.getMainLooper()
            ).addOnFailureListener {
                Logger.v("[LocationHandler] request location failed.")
                deferred.complete(null)
            }
            hasLocationCallback = true
            val waitingResult = deferred.await().also { stopLocationUpdate(locationCallback) }
            waitingResult?.let { mapper.mapToDomainModel(waitingResult.lastLocation) }
        } catch (se: SecurityException) {
            Logger.e("Location Permission is not granted: " + se.message)
            null
        } catch (exception: Exception) {
            Logger.e("Error: requestLocationUpdate()")
            null
        }
    }

    private fun stopLocationUpdate(callback: LocationCallback) {
        if (hasLocationCallback) {
            providerClient.removeLocationUpdates(callback)
            hasLocationCallback = false
        }
    }

    private fun provideLocationRequest() : LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 30 * 1000
        fastestInterval = 5 * 1000
    }
}