package ch.breatheinandout.network.airkorea.airquality

import ch.breatheinandout.domain.airquality.model.AirQuality

interface IAirQualityRemoteDataSource {
    suspend fun getAirQualityData(stationName: String): AirQuality?
}