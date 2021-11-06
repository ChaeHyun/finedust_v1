package ch.breatheinandout.database.locationandstation

import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.location.model.LocationWithNearbyStation
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import com.orhanobut.logger.Logger
import javax.inject.Inject

class LocationLocalDataSource @Inject constructor(
    val dao: LocationAndStationDao,
    val mapper: LocationAndStationEntityMapper
) : ILocationLocalDataSource {

    override suspend fun save(location: LocationPoint, nearbyStation: NearbyStation) {
        try {
            Logger.v("[SAVE.LocationEntity]")
            val domain = LocationWithNearbyStation(location, nearbyStation)
            val entity : LocationAndStationEntity = mapper.mapFromDomainModel(domain)
            dao.insert(entity)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun dropTable() {
        try {
            dao.dropTable()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun read(sidoName: String, umdName: String): LocationWithNearbyStation? {
        try {
            Logger.v("[READ.LocationEntity]")
            val entity = dao.getLocationAndStationEntity(sidoName, umdName)
            return entity?.let { data -> mapper.mapToDomainModel(data) }
        } catch (e: Exception) {
            throw e
        }
    }
}