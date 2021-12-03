package ch.breatheinandout.database.locationandstation.entity

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import javax.inject.Inject

data class NearbyStationEntity(
    val stationName: String,
    val stationAddr: String,
    val distanceKm: String
)

