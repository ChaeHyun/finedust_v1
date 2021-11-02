package ch.breatheinandout.location.model

import ch.breatheinandout.nearbystation.model.NearbyStation

data class LocationWithNearbyStation(
    val locationPoint: LocationPoint,
    val nearbyStation: NearbyStation
)
