package ch.breatheinandout.domain.airquality

import ch.breatheinandout.common.Constants
import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import ch.breatheinandout.domain.airquality.model.AirQuality
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReadAirQualityLocalUseCase @Inject constructor(
    private val localSource: IAirQualityLocalDataSource
) {
    companion object {
        private const val TIME_VALIDATION_STANDARD: Int = 4500      // 3600(1hr) + 900(offset)
    }

    private val className = ReadAirQualityLocalUseCase::class.simpleName

    suspend fun read(stationName: String) : AirQuality?= withContext(Dispatchers.IO) {
        try {
            val airQuality = localSource.get(stationName)
            if (airQuality != null && isDataTimeValid(airQuality.dataTime)) {
                Logger.v("[READ.AirQuality. VALID] it's an usable data.. -> $airQuality")
                return@withContext airQuality
            }
            Logger.v("[READ.AirQuality. INVALID] needs to request a new data to the server.. -> $airQuality")
            return@withContext null
        } catch (e: Exception) {
            Logger.e("Failed to read entity from database at $className")
            return@withContext null
        }
    }

    private  fun isDataTimeValid(source : String) : Boolean {
        return try {
            val timeZoneSeoul = TimeZone.getTimeZone(Constants.TIMEZONE_SEOUL)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).apply { timeZone = timeZoneSeoul }
            val sourceToDate = dateFormat.parse(source)!!

            val sourceTimeCal = Calendar.getInstance(timeZoneSeoul).apply { time = sourceToDate }
            val currentTimeCal = Calendar.getInstance(timeZoneSeoul)

            val log = "# CheckTime    input(now) : ${dateFormat.format(currentTimeCal.time)} -> (${currentTimeCal.time}) \n" +
                    "# CheckTime   input(TEST) : $source -> ($sourceToDate) as Date "
            Logger.v(log)

            val minusTimeAsSec = (currentTimeCal.timeInMillis - sourceTimeCal.timeInMillis) / 1000
            Logger.d("# CheckTime Validation: [${minusTimeAsSec / 60}] MIN passed.. ($minusTimeAsSec MILLIS)")
            (minusTimeAsSec < TIME_VALIDATION_STANDARD)
        } catch (e: Exception) {
            Logger.e("At TimeValidation: Exception occurred : ${e.message}")
            false
        }
    }
}