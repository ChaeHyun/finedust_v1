package ch.breatheinandout.network.airkorea.forecast

import ch.breatheinandout.network.airkorea.AirKoreaApi
import javax.inject.Inject

class ForecastRemoteDataSource @Inject constructor(
    private val airKoreaApi: AirKoreaApi
) : IForecastRemoteDataSource {

    override suspend fun getForecast(searchDate: String) : List<ForecastDto> {
        try {
            val response = airKoreaApi.getForecastData(searchDate)
            if (response.isSuccessful) {
                val forecasts: List<ForecastDto> = response.body()!!
                if (forecasts.isNotEmpty())
                    return forecasts
            }
            return emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
}