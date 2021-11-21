package ch.breatheinandout.network.airkorea.forecast

import ch.breatheinandout.domain.forecast.Forecast

interface IForecastRemoteDataSource {
    suspend fun getForecast(searchDate: String): List<Forecast>
}