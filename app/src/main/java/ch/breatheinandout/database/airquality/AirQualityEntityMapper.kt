package ch.breatheinandout.database.airquality

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.airquality.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AirQualityEntityMapper(val stationName: String) : DataMapper<AirQualityEntity, AirQuality> {
    private val timeZoneKorea = TimeZone.getTimeZone("Asia/Seoul")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).apply { timeZone = timeZoneKorea }
    override fun mapToDomainModel(data: AirQualityEntity): AirQuality {
        val details = HashMap<AirQualityType, AirQualityDetail>()
        details.apply {
            put(PM10, AirQualityDetail(data.pm10.flag, data.pm10.grade, data.pm10.value, data.pm10.value24, data.pm10.grade1h))
            put(PM25, AirQualityDetail(data.pm25.flag, data.pm25.grade, data.pm25.value, data.pm25.value24, data.pm25.grade1h))
            put(O3, AirQualityDetail(data.o3.flag, data.o3.grade, data.o3.value))
            put(CO, AirQualityDetail(data.co.flag, data.co.grade, data.co.value))
            put(NO2, AirQualityDetail(data.no2.flag, data.no2.grade, data.no2.value))
            put(SO2, AirQualityDetail(data.so2.flag, data.so2.grade, data.so2.value))
        }

        return AirQuality(
            parseToString(data.dataTime),
            data.khai.grade, data.khai.value,
            details
        )
    }

    override fun mapFromDomainModel(domain: AirQuality): AirQualityEntity {
        val khai = AirQualityValue(grade = domain.khaiGrade, value = domain.khaiValue)
        val detailKeys: List<AirQualityType> = listOf(PM10, PM25, O3, CO, NO2, SO2)
        val detailEntities = mutableListOf<AirQualityValue>()

        for (key in detailKeys) {
            when (key) {
                PM10, PM25 -> {
                    val pm: AirQualityDetail = domain.detail[key]!!
                    detailEntities.add(AirQualityValue(pm.flag, pm.grade, pm.value, pm.value24 ?: "-", pm.grade1h ?: "-"))
                }
                O3, CO, NO2, SO2 -> {
                    val others: AirQualityDetail = domain.detail[key]!!
                    detailEntities.add(AirQualityValue(others.flag, others.grade, others.value))
                }
            }
        }
        return AirQualityEntity(
            stationName = stationName,
            parseToDate(domain.dataTime),
            khai, detailEntities[PM10.order], detailEntities[PM25.order],
            detailEntities[O3.order], detailEntities[CO.order],
            detailEntities[NO2.order], detailEntities[SO2.order]
        )
    }

    fun mapToListEntity(list: List<AirQuality>) : List<AirQualityEntity> {
        return list.map { mapFromDomainModel(it) }
    }

    private fun parseToDate(source: String): Date = dateFormat.parse(source)!!
    private fun parseToString(source: Date): String = dateFormat.format(source)
}