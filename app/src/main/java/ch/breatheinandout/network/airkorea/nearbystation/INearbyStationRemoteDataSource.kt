package ch.breatheinandout.network.airkorea.nearbystation

import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import ch.breatheinandout.domain.location.model.LocationPoint

interface INearbyStationRemoteDataSource {
    suspend fun getNearbyStation(location: LocationPoint): NearbyStation?
}