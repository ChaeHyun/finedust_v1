package ch.breatheinandout.searchaddress

import ch.breatheinandout.location.model.address.AddressLine
import ch.breatheinandout.location.model.coordinates.Coordinates

data class SearchedAddress(
    val addressLine: AddressLine,
    val tmCoords: Coordinates
)