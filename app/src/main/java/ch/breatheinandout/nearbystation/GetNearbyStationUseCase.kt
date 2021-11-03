package ch.breatheinandout.nearbystation

import ch.breatheinandout.database.ILocationLocalDataSource
import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.nearbystation.model.NearbyStation
import ch.breatheinandout.network.airkorea.nearbystation.INearbyStationRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetNearbyStationUseCase @Inject constructor(
    private val stationRemoteSource: INearbyStationRemoteDataSource,
    private val locationLocalSource: ILocationLocalDataSource
) {
    sealed class Result {
        data class Success(val nearbyStation: NearbyStation) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun getNearbyStation(location: LocationPoint) : Result {
        val address = location.addressLine
        val fromDb = locationLocalSource.read(address.sidoName, address.umdName)
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
        locationLocalSource.save(location, station)
    }

    fun testDummyInput(): Coordinates {
        val tmLongitudeX = "338248.0970869"
        val tmLatitudeY = "258791.92136988"
        return Coordinates(tmLongitudeX, tmLatitudeY)
    }
}