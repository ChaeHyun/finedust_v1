package ch.breatheinandout.domain.nearbystation

import ch.breatheinandout.database.locationandstation.ILocationLocalDataSource
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveLocationAndNearbyStationUseCase @Inject constructor(
    private val locationLocalSource: ILocationLocalDataSource
) {
    private val className = SaveLocationAndNearbyStationUseCase::class.simpleName

    suspend fun save(locationPoint: LocationPoint, nearbyStation: NearbyStation) = withContext(
        Dispatchers.IO) {
        try {
            locationLocalSource.save(locationPoint, nearbyStation)

        } catch (e: Exception) {
            Logger.e("Failed to save data to database at $className")
        }
    }
}