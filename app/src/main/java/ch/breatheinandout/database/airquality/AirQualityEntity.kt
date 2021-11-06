package ch.breatheinandout.database.airquality

import androidx.room.Embedded
import androidx.room.Entity
import java.util.*

@Entity(tableName = "AirQualityEntity", primaryKeys = ["stationName", "dataTime"])
data class AirQualityEntity(
    val stationName: String,
    val dataTime: Date,

    @Embedded(prefix = "khai_") val khai : AirQualityValue,
    @Embedded(prefix = "pm10_") val pm10 : AirQualityValue,
    @Embedded(prefix = "pm25_") val pm25 : AirQualityValue,
    @Embedded(prefix = "o3_") val o3 : AirQualityValue,
    @Embedded(prefix = "co_") val co : AirQualityValue,
    @Embedded(prefix = "no2_") val no2 : AirQualityValue,
    @Embedded(prefix = "so2_") val so2 : AirQualityValue

    )

data class AirQualityValue(
    val flag: String = "-",
    val grade: String,
    val value: String,
    val value24: String = "-",
    val grade1h: String = "-"
)
