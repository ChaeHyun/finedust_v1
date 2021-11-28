package ch.breatheinandout.domain.forecast

import ch.breatheinandout.network.airkorea.forecast.IForecastRemoteDataSource
import ch.breatheinandout.screen.forecast.ForecastInfoGroupMapper
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val groupMapper: ForecastInfoGroupMapper,
    private val remoteDataSource: IForecastRemoteDataSource,
    private val saveForecastUseCase: SaveForecastInfoLocalUseCase,
    private val readForecastUseCase: ReadForecastInfoLocalUseCase
) {
    private val className = GetForecastUseCase::class.simpleName

    private val timeZoneKorea = TimeZone.getTimeZone("Asia/Seoul")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).apply { timeZone = timeZoneKorea }

    suspend fun getForecastFromLocal() : ForecastInfoGroup? {
        try {
            val currentTimeString = currentTime()
            Logger.d("currentTimeString -> $currentTimeString")
            val retrieved: List<ForecastInfo> = readForecastUseCase.read(currentTimeString)

            return if (retrieved.isNotEmpty()) {
                asForecastInfoGroup(retrieved)
            } else {
                val onlyCurrentDate = currentTimeString.substring(0,10)
                getForecastFromServer(onlyCurrentDate)
            }
        } catch (e: Exception) {
            Logger.e("Failed to get Forecast info at $className, $e")
            return null
        }

    }

    private fun asForecastInfoGroup(retrieved: List<ForecastInfo>) : ForecastInfoGroup {
        val data = arrayOfNulls<ForecastInfo>(3)
        retrieved.mapIndexed { index, info -> data[index] = info }
        return ForecastInfoGroup(data[0], data[1], data[2])
    }

    private suspend fun getForecastFromServer(searchDateForServer: String) : ForecastInfoGroup? {
        return try {
            val forecastList = remoteDataSource.getForecast(searchDateForServer)
            val forecastGroup = groupMapper.toGroup(forecastList)
            saveForecastUseCase.save(forecastGroup)
            forecastGroup
        } catch (e: Exception) {
            Logger.e("Network failed at $className ,$e")
            null
        }
    }

    private fun currentTime(): String {
        val nowWithZeroMinute: Calendar = Calendar.getInstance(timeZoneKorea)
        nowWithZeroMinute.set(Calendar.MINUTE, 0)       // cuz "yyyy-MM-dd HH:00" is the format of the Database accepts

        val hour = nowWithZeroMinute.get(Calendar.HOUR_OF_DAY)
        if (hour <= 5) {
            nowWithZeroMinute.add(Calendar.DATE, -1)
            nowWithZeroMinute.set(Calendar.HOUR_OF_DAY, 23)
        }
        return dateFormat.format(nowWithZeroMinute.time)
    }
}