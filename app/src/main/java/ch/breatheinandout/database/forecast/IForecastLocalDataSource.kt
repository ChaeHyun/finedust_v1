package ch.breatheinandout.database.forecast

import ch.breatheinandout.domain.forecast.ForecastInfo

interface IForecastLocalDataSource {
    suspend fun get(dataTime: String) : List<ForecastInfo>

    suspend fun get(dataTime: String, informCode: String) : List<ForecastInfo>

    suspend fun save(forecastList: List<ForecastInfo?>)
}