package ch.breatheinandout.domain.forecast

import java.io.Serializable

data class ForecastInfo(
    val dataTime: String,
    val informCode: String,
    val informData: String,
    val informCause: String,
    val informOverallToday: String,
    val informOverallTomorrow: String,
    val informGrade: String,
    val imageUrl: String
) : Serializable {
}