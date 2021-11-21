package ch.breatheinandout.domain.forecast

sealed class ForecastCode(val code: String)
object PM10 : ForecastCode("PM10")
object PM25 : ForecastCode("PM25")
object O3 : ForecastCode("O3")