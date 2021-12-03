package ch.breatheinandout.network.airkorea.forecast

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.forecast.model.Forecast
import javax.inject.Inject

class ForecastDtoMapper @Inject constructor(): DataMapper<ForecastDto, Forecast> {
    override fun mapToDomainModel(data: ForecastDto): Forecast {
        var imageUrl = when (data.informCode) {
            "PM10" -> data.imageUrlPm10
            "PM25" -> data.imageUrlPm25
            "O3" -> data.imageUrlO3
            else -> ""
        }
        return Forecast(
            data.dataTime.substring(0..12).plus(":00"), data.informCode, data.informData,
            data.informCause, data.informOverall, data.informGrade,
            imageUrl
        )
    }

    override fun mapFromDomainModel(domain: Forecast): ForecastDto {
        throw IllegalAccessException("Not implemented yet.")
    }

    fun mapToListDomain(list: List<ForecastDto>) : List<Forecast> {
        return list.map { mapToDomainModel(it) }
    }
}