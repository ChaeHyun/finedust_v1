package ch.breatheinandout.domain.location.model

import ch.breatheinandout.domain.nearbystation.model.NearbyStation

data class LocationWithNearbyStation(
    val locationPoint: LocationPoint,
    val nearbyStation: NearbyStation
)
