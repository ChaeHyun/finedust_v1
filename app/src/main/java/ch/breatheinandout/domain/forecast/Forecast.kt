package ch.breatheinandout.domain.forecast

import java.io.Serializable

data class Forecast(
    val dataTime: String,
    val informCode: String,
    val informData: String,
    val informCause: String,
    val informOverall: String,
    val informGrade: String,
    val imageUrl: String
) : Serializable