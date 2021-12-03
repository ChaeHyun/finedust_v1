package ch.breatheinandout.network.airkorea.forecast

import ch.breatheinandout.domain.forecast.model.Forecast

interface IForecastRemoteDataSource {
    suspend fun getForecast(searchDate: String): List<Forecast>
}