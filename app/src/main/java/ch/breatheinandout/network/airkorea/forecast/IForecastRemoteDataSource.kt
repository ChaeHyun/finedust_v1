package ch.breatheinandout.network.airkorea.forecast

interface IForecastRemoteDataSource {
    suspend fun getForecast(searchDate: String): List<ForecastDto>
}