package ch.breatheinandout.database

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun toDate(timeStamp: Long?): Date? {
        return timeStamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        return date?.time
    }
}