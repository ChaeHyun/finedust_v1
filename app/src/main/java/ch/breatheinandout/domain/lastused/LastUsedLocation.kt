package ch.breatheinandout.domain.lastused

import ch.breatheinandout.domain.location.model.LocationPoint

data class LastUsedLocation(
    val mode: String,
    val locationPoint: LocationPoint
)