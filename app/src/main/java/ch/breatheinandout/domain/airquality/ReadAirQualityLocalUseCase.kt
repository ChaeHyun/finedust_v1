package ch.breatheinandout.domain.airquality

import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadAirQualityLocalUseCase @Inject constructor(
    private val localSource: IAirQualityLocalDataSource
) {
    val className = ReadAirQualityLocalUseCase::class.simpleName

    suspend fun read(stationName: String) : AirQuality?= withContext(Dispatchers.IO) {
        try {
            val airQuality = localSource.get(stationName)
            Logger.d("[READ] airquality -> $airQuality")
            return@withContext airQuality
        } catch (e: Exception) {
            Logger.e("Failed to read entity from database.")
            return@withContext null
        }
    }
}