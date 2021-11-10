package ch.breatheinandout.domain.location.model.coordinates

import java.io.Serializable

data class Coordinates(
    val longitudeX: String,
    val latitudeY: String
) : Serializable