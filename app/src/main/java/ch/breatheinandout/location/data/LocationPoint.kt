package ch.breatheinandout.location.data

data class LocationPoint constructor(
    val addressLine: AddressLine,
    val wgsCoords: Coordinates
) {
}

