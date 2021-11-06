package ch.breatheinandout.domain.airquality

data class AirQuality(
    val dataTime: String,
    val khaiGrade: String = "-",        // 통합대기환경수치
    val khaiValue: String,
    val detail: Map<AirQualityType, AirQualityDetail>
)

sealed class AirQualityType(val order: Int)
object PM10 : AirQualityType(0)         // 미세먼지
object PM25 : AirQualityType(1)         // 초미세먼지
object O3 : AirQualityType(2)           // 오존
object CO : AirQualityType(3)           // 일산화탄소
object NO2 : AirQualityType(4)          // 이산화질소
object SO2 : AirQualityType(5)          // 아황산가스

data class AirQualityDetail(
    val flag: String = "-",         // The measurement device is currently working or not
    val grade: String = "-",
    val value: String = "-",
    val value24: String? = "-",     // only available for pm10, pm25
    val grade1h: String? = "-"      // only available for pm10, pm25
)

