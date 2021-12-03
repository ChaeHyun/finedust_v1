package ch.breatheinandout.domain.forecast

import ch.breatheinandout.database.forecast.IForecastLocalDataSource
import ch.breatheinandout.domain.forecast.model.ForecastInfo
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReadForecastInfoLocalUseCase @Inject constructor(
    private val localSource: IForecastLocalDataSource
) {
    private val className = ReadForecastInfoLocalUseCase::class.simpleName

    /** @param now "yyyy-MM-dd HH:00" CurrentTime as String */
    suspend fun read(now: String): List<ForecastInfo> = withContext(Dispatchers.IO) {
        try {
            val dataTime = hitRangeOfHours(now)
            val infoList: List<ForecastInfo> = localSource.get(dataTime)

            if (infoList.isNotEmpty()) {
                Logger.i("[READ.Forecast. VALID] it's still usable data.. -> $infoList")
                return@withContext infoList
            }
            Logger.i("[READ.Forecast. INVALID] needs to request a new data to the server.. -> $infoList")
            return@withContext emptyList()
        } catch (e: Exception) {
            Logger.e("Failed to read entity from database at $className")
            return@withContext emptyList()
        }
    }

    private fun hitRangeOfHours(now: String) : String {
        val timeZoneKorea = TimeZone.getTimeZone("Asia/Seoul")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:00", Locale.KOREA).apply { timeZone = timeZoneKorea }
        val nowCalendar = Calendar.getInstance(timeZoneKorea)
        nowCalendar.time = dateFormat.parse(now)!!
        val ranged = setupRangeOfHours(nowCalendar)

        return dateFormat.format(ranged.time)
    }

    /** The forecast info is updated 4 time per day.
     *  It's { 05 , 11 , 17 , 23 } as the hour of day. */
    private fun setupRangeOfHours(source: Calendar): Calendar {
        val hour : Int = source.get(Calendar.HOUR_OF_DAY)
        when {
            hour < 5 -> {
                source.add(Calendar.DATE, -1)
                source.set(Calendar.HOUR_OF_DAY, 23)
            }
            hour < 11 -> {
                source.set(Calendar.HOUR_OF_DAY, 5)
            }
            hour < 17 -> {
                source.set(Calendar.HOUR_OF_DAY, 11)
            }
            hour < 23 -> {
                source.set(Calendar.HOUR_OF_DAY, 17)
            }
            hour < 24 -> {
                source.set(Calendar.HOUR_OF_DAY, 23)
            }
        }
        return source
    }
}