package ch.breatheinandout.domain.nearbystation

import ch.breatheinandout.domain.location.GetStoredLocationUseCase
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import ch.breatheinandout.network.airkorea.nearbystation.INearbyStationRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetNearbyStationUseCase @Inject constructor(
    private val stationRemoteSource: INearbyStationRemoteDataSource,
    private val getStoredLocationUseCase: GetStoredLocationUseCase,
    private val saveLocationAndNearbyStationUseCase: SaveLocationAndNearbyStationUseCase
) {
    sealed class Result {
        data class Success(val nearbyStation: NearbyStation) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun getNearbyStation(location: LocationPoint) : Result {
        val address = location.addressLine
//        val fromDb = locationLocalSource.read(address.sidoName, address.umdName)
        val fromDb = getStoredLocationUseCase.getStoredLocation(address.sidoName, address.umdName)
        return fromDb?.let { Result.Success(it.nearbyStation) }
            ?: getNearbyStationFromServer(location)
    }

    private suspend fun getNearbyStationFromServer(location: LocationPoint) : Result {
        return try {
            val result: NearbyStation? = stationRemoteSource.getNearbyStation(location)

            result?.let { station ->
                Logger.d("Save in database -> $result")
                saveInLocal(location, station)
                Result.Success(station)
            } ?: Result.Failure("Response is empty", NullPointerException())
        } catch (e: Exception) {
            Result.Failure("Network failed", e)
        }
    }

    private suspend fun saveInLocal(location: LocationPoint, station: NearbyStation) {
        saveLocationAndNearbyStationUseCase.save(location, station)
    }
}