package ch.breatheinandout.database.forecast

import ch.breatheinandout.database.forecast.entity.ForecastEntityMapper
import ch.breatheinandout.domain.forecast.model.ForecastInfo
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ForecastLocalDataSource @Inject constructor(
    private val dao: ForecastDao,
    private val mapper: ForecastEntityMapper
) : IForecastLocalDataSource {

    private val timeZoneKorea = TimeZone.getTimeZone("Asia/Seoul")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).apply { timeZone = timeZoneKorea }

    /** @param dataTime "yyyy-MM-dd HH:mm" */
    override suspend fun get(dataTime: String): List<ForecastInfo> {
        try {
            val searchDate: Date = dateFormat.parse(dataTime)!!
            val entities = dao.getForecast(searchDate)
            return mapper.mapToListDomain(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun get(dataTime: String, informCode: String): List<ForecastInfo> {
        try {
            val searchDate: Date = dateFormat.parse(dataTime)!!
            val entities = dao.getForecast(searchDate, informCode)
            Logger.d("check -> $entities")

            return mapper.mapToListDomain(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun save(forecastList: List<ForecastInfo?>) {
        try {
            val entities = mapper.mapToEntities(forecastList.mapNotNull { it })
            dao.insert(entities)
        } catch (e: Exception) {
            throw e
        }
    }
}