package ch.breatheinandout.domain.airquality.model

sealed class AirQualityType(val order: Int)
object PM10 : AirQualityType(0)         // 미세먼지
object PM25 : AirQualityType(1)         // 초미세먼지
object O3 : AirQualityType(2)           // 오존
object CO : AirQualityType(3)           // 일산화탄소
object NO2 : AirQualityType(4)          // 이산화질소
object SO2 : AirQualityType(5)          // 아황산가스