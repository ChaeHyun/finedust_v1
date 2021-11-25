package ch.breatheinandout.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToMap(source: String?): Map<String, String>? {
        val typeToken = object: TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(source, typeToken)
    }

    @TypeConverter
    fun mapToString(value: Map<String, String>?): String? {
        return if (value == null) "" else gson.toJson(value)
    }
}