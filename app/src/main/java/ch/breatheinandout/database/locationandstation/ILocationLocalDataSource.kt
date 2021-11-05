package ch.breatheinandout.database.locationandstation

import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.location.model.LocationWithNearbyStation
import ch.breatheinandout.domain.nearbystation.model.NearbyStation

interface ILocationLocalDataSource {
    suspend fun save(location: LocationPoint, nearbyStation: NearbyStation)
    suspend fun read(sidoName: String, umdName: String) : LocationWithNearbyStation?
    suspend fun dropTable()
}
