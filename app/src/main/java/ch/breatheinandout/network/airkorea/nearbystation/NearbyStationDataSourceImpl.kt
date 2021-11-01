package ch.breatheinandout.network.airkorea.nearbystation

import ch.breatheinandout.nearbystation.model.NearbyStation
import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.network.airkorea.AirKoreaApi
import javax.inject.Inject

class NearbyStationDataSourceImpl @Inject constructor(
    private val airKoreaApi: AirKoreaApi
) : NearbyStationDataSource {

    override suspend fun getNearbyStation(location: LocationPoint) : NearbyStation? {
        val tmCoords = location.tmCoords
        try {
            val response = airKoreaApi.getNearbyStationListByTmCoordinates(tmCoords.longitudeX, tmCoords.latitudeY)
            if (response.isSuccessful) {
                val nearbyStations: List<NearbyStationDto> = response.body()!!
                if (nearbyStations.isNotEmpty())
                    return nearbyStations[0].mapToDomain()
            }
            return null
        } catch (e: Exception) {
            // Network Failed.
            throw e
        }
    }
}