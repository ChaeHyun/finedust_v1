package ch.breatheinandout.domain.airquality

import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import ch.breatheinandout.domain.airquality.model.AirQuality
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadAirQualityLocalUseCase @Inject constructor(
    private val localSource: IAirQualityLocalDataSource
) {
    private val className = ReadAirQualityLocalUseCase::class.simpleName

    suspend fun read(stationName: String) : AirQuality?= withContext(Dispatchers.IO) {
        try {
            val airQuality = localSource.get(stationName)
            Logger.v("[READ.AirQuality] -> $airQuality")
            return@withContext airQuality
        } catch (e: Exception) {
            Logger.e("Failed to read entity from database at $className")
            return@withContext null
        }
    }
}