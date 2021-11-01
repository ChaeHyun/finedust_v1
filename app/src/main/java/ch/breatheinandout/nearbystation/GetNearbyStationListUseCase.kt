package ch.breatheinandout.nearbystation

import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.nearbystation.model.NearbyStation
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationDataSource
import javax.inject.Inject

class GetNearbyStationListUseCase @Inject constructor(
    private val nearbyStationDataSource: NearbyStationDataSource
) {
    sealed class Result {
        data class Success(val nearbyStation: NearbyStation) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun getNearbyStation(location: LocationPoint) : Result {
        return try {
            val result = nearbyStationDataSource.getNearbyStation(location)
            result?.let { station -> Result.Success(station) } ?: Result.Failure("Response is empty", NullPointerException())
        } catch (e: Exception) {
            Result.Failure("Network failed", e)
        }
    }

    fun testDummyInput(): Coordinates {
        val tmLongitudeX = "338248.0970869"
        val tmLatitudeY = "258791.92136988"
        return Coordinates(tmLongitudeX, tmLatitudeY)
    }
}