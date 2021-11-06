package ch.breatheinandout.database.airquality

import ch.breatheinandout.domain.airquality.model.AirQuality


interface IAirQualityLocalDataSource {
    suspend fun get(stationName: String) : AirQuality?

    suspend fun save(stationName: String, data: AirQuality)

    suspend fun save(stationName: String, list: List<AirQuality>)

    suspend fun update(stationName: String, data: AirQuality)

    suspend fun dropTable()
}