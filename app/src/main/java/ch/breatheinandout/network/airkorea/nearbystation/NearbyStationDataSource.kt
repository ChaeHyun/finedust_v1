package ch.breatheinandout.network.airkorea.nearbystation

import ch.breatheinandout.nearbystation.model.NearbyStation
import ch.breatheinandout.location.model.LocationPoint

interface NearbyStationDataSource {
    suspend fun getNearbyStation(location: LocationPoint): NearbyStation?
}