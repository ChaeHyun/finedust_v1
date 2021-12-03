package ch.breatheinandout.database.forecast.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import ch.breatheinandout.database.converters.MapTypeConverter
import java.util.*

@Entity(tableName = "ForecastEntity", primaryKeys = ["dataTime", "informCode"])
data class ForecastEntity (
    val dataTime: Date,
    val informCode: String,
    val informData: String,
    val informCause: String,
    val informOverallToday: String,
    val informOverallTomorrow: String,
    val imageUrl: String,
    @TypeConverters(MapTypeConverter::class)
    val informGrade: Map<String, String>
)