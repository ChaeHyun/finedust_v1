package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.coordinates.Coordinates

data class SearchedAddress(
    val addressLine: AddressLine,
    val tmCoords: Coordinates
)