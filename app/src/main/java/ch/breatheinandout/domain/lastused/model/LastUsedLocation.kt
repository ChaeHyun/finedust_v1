package ch.breatheinandout.domain.lastused.model

import ch.breatheinandout.domain.location.model.LocationPoint

data class LastUsedLocation(
    val mode: String,
    val locationPoint: LocationPoint
)