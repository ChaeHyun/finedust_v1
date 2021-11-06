package ch.breatheinandout.domain.airquality.model

data class AirQuality(
    val dataTime: String,
    val khaiGrade: String = "-",        // 통합대기환경수치
    val khaiValue: String,
    val detail: Map<AirQualityType, AirQualityDetail>
)


