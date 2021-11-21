package ch.breatheinandout.domain.forecast

import java.io.Serializable

data class ForecastInfoGroup(
    val pm10: ForecastInfo?,
    val pm25: ForecastInfo?,
    val o3: ForecastInfo?
) : Serializable