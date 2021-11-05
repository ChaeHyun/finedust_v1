package ch.breatheinandout.domain.location

import ch.breatheinandout.domain.location.provider.FindAddressLine
import ch.breatheinandout.domain.location.provider.LocationHandler
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.searchaddress.SearchedAddress
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler,
    private val findAddressLine: FindAddressLine,
    private val transCoordinatesUseCase: TransCoordinatesUseCase,
    private val getStoredLocationUseCase: GetStoredLocationUseCase
) {

    sealed class Result {
        data class Success(val location: LocationPoint) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }


    suspend fun update(address: SearchedAddress?) : Result = withContext(Dispatchers.IO) {
        if (address != null) {
            return@withContext Result.Success(LocationPoint(address.addressLine, address.tmCoords, null))
        }

        val wgsCoords = locationHandler.getLocationDeferred()
        if (wgsCoords == null) {
            Logger.e("wgsCoord is NULL.")
            return@withContext Result.Failure("Failed to find a WGS coordinates.", NullPointerException())
        } else {
            val addressLine = findAddressLine.findAddress(wgsCoords)
            val retrieved = getStoredLocationUseCase.getStoredLocation(addressLine.sidoName, addressLine.umdName)
            retrieved?.let {
                // TODO before return Success: this retrieved data needs to save somewhere for the last-used data.
                return@withContext Result.Success(retrieved.locationPoint)
            }

            val tmCoords = handleTransCoordsResult(transCoordinatesUseCase.translateWgsToTmCoordinates(wgsCoords))
            return@withContext tmCoords?.let {
                val lPoint = LocationPoint(addressLine, tmCoords, wgsCoords)
                Result.Success(lPoint)
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