package ch.breatheinandout.nearbystation

import ch.breatheinandout.NearbyStation
import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.network.airkorea.AirKoreaApi
import ch.breatheinandout.network.airkorea.NearbyStationDto
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetNearbyStationListUseCase @Inject constructor(
    private val airKoreaApi: AirKoreaApi
) {
    sealed class Result {
        data class Success(val nearbyStation: NearbyStation) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }
    private val className = GetNearbyStationListUseCase::class.simpleName

    suspend fun getNearbyStation(location: LocationPoint): Result {
        val tmCoords = location.tmCoords

        try {
            val response = airKoreaApi.getNearbyStationListByTmCoordinates(tmCoords.longitudeX, tmCoords.latitudeY)
            if (response.isSuccessful) {
                val nearbyStations: List<NearbyStationDto> = response.body() ?: return Result.Failure(
                    "No data found at $className", NullPointerException())

                // Mapping StationDto -> StationDomain
                val nearbyStation = nearbyStations[0].mapToDomain()
                return Result.Success(nearbyStation)
            } else {
                return Result.Failure("[HTTP.Unsuccessful] header.status:[${response.code()}, ${response.message()}], ${response.errorBody()}", NullPointerException())
            }
        } catch (e: Exception) {
            Logger.e("Exception occurred. at $className")
            return Result.Failure("Network failed at $className", e)
        }
    }

    fun testDummyInput(): Coordinates {
        val tmLongitudeX = "338248.0970869"
        val tmLatitudeY = "258791.92136988"
        return Coordinates(tmLongitudeX, tmLatitudeY)
    }
}