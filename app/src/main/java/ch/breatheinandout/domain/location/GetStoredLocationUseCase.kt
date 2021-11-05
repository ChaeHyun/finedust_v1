package ch.breatheinandout.domain.location

import ch.breatheinandout.database.locationandstation.ILocationLocalDataSource
import ch.breatheinandout.domain.location.model.LocationWithNearbyStation
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStoredLocationUseCase @Inject constructor(
    private val locationLocalSource: ILocationLocalDataSource
) {
    private val className = GetStoredLocationUseCase::class.simpleName

    suspend fun getStoredLocation(sidoName: String, umdName: String): LocationWithNearbyStation? = withContext(Dispatchers.IO) {
        try {
            return@withContext locationLocalSource.read(sidoName, umdName)
                ?: return@withContext null
        } catch (e: Exception) {
            Logger.e("Failed to get data from database at $className")
            return@withContext null
        }
    }
}