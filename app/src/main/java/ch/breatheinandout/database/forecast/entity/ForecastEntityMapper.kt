package ch.breatheinandout.database.forecast.entity

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.forecast.model.ForecastInfo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ForecastEntityMapper @Inject constructor() : DataMapper<ForecastEntity, ForecastInfo> {
    private val timeZoneKorea = TimeZone.getTimeZone("Asia/Seoul")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).apply { timeZone = timeZoneKorea }
    override fun mapToDomainModel(data: ForecastEntity): ForecastInfo {
        return ForecastInfo(
            parseToString(data.dataTime),
            data.informCode,
            data.informData,
            data.informCause,
            data.informOverallToday,
            data.informOverallTomorrow,
            data.imageUrl
        ).also { it.grades.putAll(data.informGrade) }
    }

    override fun mapFromDomainModel(domain: ForecastInfo): ForecastEntity {
        return ForecastEntity(
            parseToDate(domain.dataTime),
            domain.informCode,
            domain.informData,
            domain.informCause,
            domain.informOverallToday,
            domain.informOverallTomorrow,
            domain.imageUrl,
            domain.grades
        )
    }

    fun mapToListDomain(entities: List<ForecastEntity>) : List<ForecastInfo> {
        return entities.map { mapToDomainModel(it) }
    }

    fun mapToEntities(domains: List<ForecastInfo>) : List<ForecastEntity> {
        return domains.map { mapFromDomainModel(it) }
    }

    private fun parseToDate(source: String): Date = dateFormat.parse(source)!!
    private fun parseToString(source: Date): String = dateFormat.format(source)
}