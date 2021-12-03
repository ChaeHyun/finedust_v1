package ch.breatheinandout.domain.forecast.model

import java.io.Serializable

data class ForecastInfo(
    val dataTime: String,
    val informCode: String,
    val informData: String,
    val informCause: String,
    val informOverallToday: String,
    val informOverallTomorrow: String,
    val imageUrl: String,
    val grades: MutableMap<String, String> = mutableMapOf(
        "서울" to "", "인천" to "",
        "경기북부" to "", "경기남부" to "",
        "영서" to "", "영동" to "",
        "충북" to "", "충남" to "",
        "대전" to "", "세종" to "",
        "광주" to "", "전북" to "", "전남" to "",
        "대구" to "", "경북" to "",
        "부산" to "", "울산" to "", "경남" to "",
        "제주" to ""
    )

) : Serializable