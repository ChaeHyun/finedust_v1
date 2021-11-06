package ch.breatheinandout.domain.searchaddress.model

import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import java.io.Serializable

data class SearchedAddress(
    val addressLine: AddressLine,
    val tmCoords: Coordinates
) : Serializable