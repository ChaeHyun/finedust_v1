package ch.breatheinandout.domain.location

import ch.breatheinandout.common.Constants
import ch.breatheinandout.common.utils.FeatureAvailability
import ch.breatheinandout.domain.lastused.LastUsedLocation
import ch.breatheinandout.domain.lastused.ReadLastUsedLocationUseCase
import ch.breatheinandout.domain.lastused.SaveLastUsedLocationUseCase
import ch.breatheinandout.domain.location.provider.FindAddressLine
import ch.breatheinandout.domain.location.provider.LocationHandler
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import com.orhanobut.logger.Logger
import hilt_aggregated_deps._ch_breatheinandout_screen_airquality_AirQualityViewModel_HiltModules_KeyModule
import kotlinx.coroutines.*
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler,
    private val findAddressLine: FindAddressLine,
    private val featureAvailability: FeatureAvailability,
    private val transCoordinatesUseCase: TransCoordinatesUseCase,
    private val getStoredLocationUseCase: GetStoredLocationUseCase,
    private val saveLastUsedLocationUseCase: SaveLastUsedLocationUseCase,
    private val readLastUsedLocationUseCase: ReadLastUsedLocationUseCase
) {
    sealed class Result {
        data class Success(val location: LocationPoint) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    companion object {
        const val GPS = Constants.MODE_GPS
        const val ADDR = Constants.MODE_ADDR

        // Fail Message
        const val FAIL_LOCATION_HANDLER = "Failed to find a WGS coordinates."
        const val FAIL_ACTIVATE_GPS = "Please turn on the gps."
    }

    suspend fun update(address: SearchedAddress?) : Result = withContext(Dispatchers.IO) {
        if (address == null) {
            val lastUsedLocation = hasValidLastLocation()
            Logger.d("[HasValidLastLocation()] -> $lastUsedLocation")
            lastUsedLocation?.let { return@withContext Result.Success(lastUsedLocation.locationPoint) }
        }
        else if (address.addressLine.addr != Constants.FORCE_GPS) {
            Logger.d("[Updates with selected address...] -> $address")
            val lastAddr = LocationPoint(address.addressLine, address.tmCoords, null)
            saveLastUsedLocationUseCase.save(ADDR, lastAddr)
            return@withContext Result.Success(LocationPoint(address.addressLine, address.tmCoords, null))
        }

        if (!featureAvailability.isGpsFeatureOn())
            return@withContext Result.Failure(FAIL_ACTIVATE_GPS, NullPointerException())

        return@withContext calculateGpsCoordinates()
    }

    private fun hasValidLastLocation() : LastUsedLocation? {
        return try {
            val lastLocation = readLastUsedLocationUseCase.read()
            when (lastLocation.mode) {
                ADDR -> lastLocation
                else -> null
            }
        } catch (e: Exception) {
            // In case that there is no data for LastUsedLocation yet.
            null
        }
    }

    private suspend fun calculateGpsCoordinates() : Result {
        Logger.d("[Calculates a New Gps Coords...]")
        val wgsCoords = locationHandler.getLocationDeferred()
        if (wgsCoords == null) {
            Logger.e("wgsCoord is NULL.")
            // TODO: GPS is not available. Ask user to turn on the GPS.
            return Result.Failure(FAIL_LOCATION_HANDLER, NullPointerException())
        } else {
            val addressLine = findAddressLine.findAddress(wgsCoords)
            val retrieved = getStoredLocationUseCase.getStoredLocation(addressLine.sidoName, addressLine.umdName)
            retrieved?.let {
                saveLastUsedLocationUseCase.save(GPS, retrieved.locationPoint)
                return Result.Success(retrieved.locationPoint)
            }

            val tmCoords = handleTransCoordsResult(transCoordinatesUseCase.translateWgsToTmCoordinates(wgsCoords))
            return tmCoords?.let {
                val locPoint = LocationPoint(addressLine, tmCoords, wgsCoords)
                saveLastUsedLocationUseCase.save(GPS, locPoint)
                Result.Success(locPoint)
            } ?: Result.Failure("Failed to translate coords", NullPointerException())
        }
    }

    private fun handleTransCoordsResult(result: TransCoordinatesUseCase.Result) : Coordinates? {
        return when (result) {
            is TransCoordinatesUseCase.Result.Success -> { result.tmCoords }
            is TransCoordinatesUseCase.Result.Failure -> { null }
        }
    }

}