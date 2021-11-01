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
        val coordsResult = locationHandler.getLastLocation()
        Logger.d("check result : $coordsResult")
        return@withContext when (coordsResult) {
            is LocationHandler.Result.Success -> {
                val addressLine = findAddressLine.findAddress(coordsResult.wgsCoords)
                val tmCoords = handleTransCoordsResult(transCoordinatesUseCase.translateWgsToTmCoordinates(coordsResult.wgsCoords))
                tmCoords?.let {
                    val locPoint = LocationPoint(addressLine, tmCoords, coordsResult.wgsCoords)
                    Result.Success(locPoint)
                } ?: Result.Failure("Failed to translate coords", NullPointerException())
            }
            is LocationHandler.Result.Failure -> {
                Result.Failure("Failed to find a coordinates", NullPointerException())
            }
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