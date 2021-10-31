package ch.breatheinandout.location.model

import ch.breatheinandout.location.model.address.AddressLine
import ch.breatheinandout.location.model.coordinates.Coordinates

/** It contains a location point with GPS coordinates and it's address info. */
data class LocationPoint constructor(
    val addressLine: AddressLine,
    val tmCoords: Coordinates,
    val wgsCoords: Coordinates?
) {
}

