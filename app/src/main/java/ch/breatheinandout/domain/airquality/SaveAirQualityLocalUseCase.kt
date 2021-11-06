package ch.breatheinandout.domain.airquality

import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SaveAirQualityLocalUseCase @Inject constructor(
    private val localSource: IAirQualityLocalDataSource
) {
    private val className = SaveAirQualityLocalUseCase::class.simpleName

    // TODO: 여기서 withContext 하거나 source 에서 withContext 하던가 2개 중 하나만 하면 되지 않을까?
    suspend fun save(stationName: String, data: AirQuality) = withContext(Dispatchers.IO) {
        try {
            Logger.d("[Save] AirQuality into the database")
            localSource.save(stationName, data)
        } catch (e: Exception) {
            Logger.e("Failed to save data at $className")
        }
    }
}