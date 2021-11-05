package ch.breatheinandout.network.airkorea.airquality

import ch.breatheinandout.domain.airquality.AirQuality

interface IAirQualityRemoteDataSource {
    suspend fun getAirQualityData(stationName: String): AirQuality?
}