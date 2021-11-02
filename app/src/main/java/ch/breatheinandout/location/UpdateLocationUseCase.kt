package ch.breatheinandout.location

import ch.breatheinandout.location.provider.FindAddressLine
import ch.breatheinandout.location.provider.LocationHandler
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.location.model.LocationPoint
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationHandler: LocationHandler,
    private val findAddressLine: FindAddressLine,
    private val transCoordinatesUseCase: TransCoordinatesUseCase
) {

    sealed class Result {
        data class Success(val location: LocationPoint) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun update() : Result = withContext(Dispatchers.IO) {
        val wgsCoords = locationHandler.getLocationDeferred()

        if (wgsCoords == null) {
            Logger.e("wgsCoord is NULL.")
            return@withContext Result.Failure("Failed to find a WGS coordinates.", NullPointerException())
        } else {
            val addressLine = findAddressLine.findAddress(wgsCoords)
            val tmCoords = handleTransCoordsResult(transCoordinatesUseCase.translateWgsToTmCoordinates(wgsCoords))
            return@withContext tmCoords?.let {
                val lPoint = LocationPoint(addressLine, tmCoords, wgsCoords)
                Result.Success(lPoint)
            } ?: Result.Failure("Failed to translate coords", NullPointerException())
        }
    }

    private fun handleTransCoordsResult(result: TransCoordinatesUseCase.Result) : Coordinates? {
        return when (result) {
            is TransCoordinatesUseCase.Result.Success -> {
                result.tmCoords
            }
            is TransCoordinatesUseCase.Result.Failure -> {
                null
            }
        }
    }

}