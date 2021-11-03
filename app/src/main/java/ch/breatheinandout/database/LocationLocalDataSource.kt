package ch.breatheinandout.database

import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.location.model.LocationWithNearbyStation
import ch.breatheinandout.nearbystation.model.NearbyStation
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationLocalDataSource(
    val dao: LocationAndStationDao,
    val mapper: LocationAndStationEntityMapper
) : ILocationLocalDataSource {

    override suspend fun save(location: LocationPoint, nearbyStation: NearbyStation) = withContext(
        Dispatchers.IO) {
        Logger.v("[SAVE.LocationEntity]")
        val domain = LocationWithNearbyStation(location, nearbyStation)
        val entity : LocationAndStationEntity = mapper.mapFromDomainModel(domain)
        dao.insert(entity)
    }

    override suspend fun dropTable() = withContext(Dispatchers.IO){
        dao.dropTable()
    }

    override suspend fun read(sidoName: String, umdName: String): LocationWithNearbyStation? =
        withContext(Dispatchers.IO){
        Logger.v("[READ.LocationEntity]")
        val entity = dao.getLocationAndStationEntity(sidoName, umdName)
        return@withContext entity?.let { data -> mapper.mapToDomainModel(data) }
    }
}