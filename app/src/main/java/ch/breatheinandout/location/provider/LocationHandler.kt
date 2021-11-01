package ch.breatheinandout.location.provider

import android.os.Looper
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.location.model.coordinates.CoordinatesMapper
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LocationHandler @Inject constructor(
    private val providerClient: FusedLocationProviderClient,
    private val mapper: CoordinatesMapper
) {

    sealed class Result {
        data class Success(val wgsCoords: Coordinates) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    private var deferredValue: Result = Result.Failure(MSG_FAILED, NullPointerException())
    private val latch: CountDownLatch = CountDownLatch(1)

    private var hasLocationCallback = false
    private val locationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            deferredValue = Result.Success(mapper.mapToDomainModel(result.lastLocation))
            latch.countDown()
        }
    }

    suspend fun getLastLocation(): Result = withContext(Dispatchers.IO) {
        try {
            return@withContext startLocationUpdate(locationCallback)
        } catch (exception: SecurityException) {
            return@withContext Result.Failure(MSG_PERMISSION_ERROR, exception).also { stopLocationUpdate(locationCallback) }
        }
    }

    private fun startLocationUpdate(locationCallback: LocationCallback) : Result {
        Logger.v("[ startLocationUpdate() - isRunning ]")
        return try {
            hasLocationCallback = true
            providerClient.requestLocationUpdates(
                provideLocationRequest(),
                locationCallback,
                Looper.getMainLooper()
            )

            latch.await(500, TimeUnit.MILLISECONDS).also { stopLocationUpdate(locationCallback) }
            deferredValue
        } catch (exception: SecurityException) {
            Result.Failure(MSG_PERMISSION_ERROR, exception).also { stopLocationUpdate(locationCallback) }
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

    companion object {
        const val MSG_FAILED = "Failed to update a location."
        const val MSG_PERMISSION_ERROR = "Location Permission not allowed."
    }
}