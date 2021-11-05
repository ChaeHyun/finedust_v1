package ch.breatheinandout.database.locationandstation

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.nearbystation.model.NearbyStation
import javax.inject.Inject

data class NearbyStationEntity(
    val stationName: String,
    val stationAddr: String,
    val distanceKm: String
)

class NearbyStationEntityMapper @Inject constructor(): DataMapper<NearbyStationEntity, NearbyStation> {
    override fun mapToDomainModel(data: NearbyStationEntity): NearbyStation {
        return NearbyStation(
            data.stationName,
            data.stationAddr,
            data.distanceKm
        )
    }

    override fun mapFromDomainModel(domain: NearbyStation): NearbyStationEntity {
        return NearbyStationEntity(
            domain.stationName,
            domain.stationAddressLine,
            domain.distance
        )
    }
}