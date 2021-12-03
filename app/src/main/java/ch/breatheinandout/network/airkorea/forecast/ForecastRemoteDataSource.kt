package ch.breatheinandout.network.airkorea.forecast

import ch.breatheinandout.domain.forecast.model.Forecast
import ch.breatheinandout.network.airkorea.AirKoreaApi
import javax.inject.Inject

class ForecastRemoteDataSource @Inject constructor(
    private val airKoreaApi: AirKoreaApi,
    private val dtoMapper: ForecastDtoMapper
) : IForecastRemoteDataSource {

    override suspend fun getForecast(searchDate: String) : List<Forecast> {
        try {
            val response = airKoreaApi.getForecastData(searchDate)
            if (response.isSuccessful) {
                val dtoList: List<ForecastDto> = response.body()!!
                if (dtoList.isNotEmpty())
                    return dtoMapper.mapToListDomain(dtoList)
            }
            return emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
}