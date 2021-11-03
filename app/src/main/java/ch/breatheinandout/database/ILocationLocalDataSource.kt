package ch.breatheinandout.database

import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.location.model.LocationWithNearbyStation
import ch.breatheinandout.nearbystation.model.NearbyStation

interface ILocationLocalDataSource {
    suspend fun save(location: LocationPoint, nearbyStation: NearbyStation)
    suspend fun read(sidoName: String, umdName: String) : LocationWithNearbyStation?
    suspend fun dropTable()
}
