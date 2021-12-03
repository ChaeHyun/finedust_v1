package ch.breatheinandout.domain.forecast.model

import java.io.Serializable

data class ForecastGroup(
    val pm10: List<Forecast>,
    val pm25: List<Forecast>,
    val o3: List<Forecast>
) : Serializable