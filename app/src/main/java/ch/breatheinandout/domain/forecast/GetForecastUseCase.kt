package ch.breatheinandout.domain.forecast

import ch.breatheinandout.network.airkorea.forecast.IForecastRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val remoteDataSource: IForecastRemoteDataSource
) {

    suspend fun forecast(searchDate: String) : ForecastInfoGroup {
        val result = remoteDataSource.getForecast(searchDate)
        Logger.i("$result")

        return groupByInformCode(result)
    }

    private fun groupByInformCode(list: List<Forecast>): ForecastInfoGroup {
        val pm10: MutableList<Forecast> = mutableListOf()
        val pm25: MutableList<Forecast> = mutableListOf()
        val o3: MutableList<Forecast> = mutableListOf()
        var infoPm10: ForecastInfo? = null
        var infoPm25: ForecastInfo? = null
        var infoO3: ForecastInfo? = null

        list.forEach {
            when (it.informCode) {
                PM10.code -> pm10.add(it)
                PM25.code -> pm25.add(it)
                O3.code -> o3.add(it)
            }
        }
        if (pm10.size >= 2) {
            infoPm10 = mapToForecastInfo(pm10)
        }
        if (pm25.size >= 2) {
            infoPm25 = mapToForecastInfo(pm25)
        }
        if (o3.size >= 2) {
            infoO3 = mapToForecastInfo(o3)
        }

        return ForecastInfoGroup(infoPm10, infoPm25, infoO3)
    }
    
    private fun mapToForecastInfo(list: List<Forecast>) : ForecastInfo {
        if (list.size < 2)
            throw IllegalArgumentException()

        return ForecastInfo(
            list[0].dataTime, list[0].informCode,
            list[0].informData, list[0].informCause,
            list[0].informOverall, list[1].informOverall,
            list[0].informGrade, list[0].imageUrl
        )
    }
}