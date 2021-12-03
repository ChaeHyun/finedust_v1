package ch.breatheinandout.domain.forecast

import ch.breatheinandout.database.forecast.IForecastLocalDataSource
import ch.breatheinandout.domain.forecast.model.ForecastInfoGroup
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveForecastInfoLocalUseCase @Inject constructor(
    private val localSource: IForecastLocalDataSource
) {
    private val className = SaveForecastInfoLocalUseCase::class.simpleName

    suspend fun save(group: ForecastInfoGroup) = withContext(Dispatchers.IO) {
        try {
            localSource.save(listOfNotNull(group.pm10, group.pm25, group.o3))
        } catch (e: Exception) {
            Logger.e("Failed to save data at $className")
        }
    }
}