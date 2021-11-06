package ch.breatheinandout.domain.airquality.model

data class AirQualityDetail(
    val flag: String = "-",         // The measurement device is currently working or not
    val grade: String = "-",
    val value: String = "-",
    val value24: String? = "-",     // only available for pm10, pm25
    val grade1h: String? = "-"      // only available for pm10, pm25
)