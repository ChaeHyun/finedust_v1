package ch.breatheinandout.network.airkorea.airquality

import ch.breatheinandout.domain.airquality.AirQuality
import ch.breatheinandout.network.airkorea.AirKoreaApi
import javax.inject.Inject

class AirQualityRemoteDataSource @Inject constructor(
    private val airKoreaApi: AirKoreaApi
) : IAirQualityRemoteDataSource {

    override suspend fun getAirQualityData(stationName: String) : AirQuality? {
        try {
            val response = airKoreaApi.getAirQualityInRealTime(stationName)
            if (response.isSuccessful) {
                val dto: List<AirQualityDto> = response.body() ?: return null
                return dto[0].mapToDomain()
            }
            return null
        } catch (e: Exception) {
            // Network failed.
            throw e
        }
    }
}