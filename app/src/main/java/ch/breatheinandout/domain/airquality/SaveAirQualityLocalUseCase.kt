package ch.breatheinandout.domain.airquality

import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import ch.breatheinandout.domain.airquality.model.AirQuality
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SaveAirQualityLocalUseCase @Inject constructor(
    private val localSource: IAirQualityLocalDataSource
) {
    private val className = SaveAirQualityLocalUseCase::class.simpleName

    suspend fun save(stationName: String, data: AirQuality) = withContext(Dispatchers.IO) {
        try {
            Logger.v("[Save.AirQuality]")
            localSource.save(stationName, data)
        } catch (e: Exception) {
            Logger.e("Failed to save data at $className")
        }
    }
}