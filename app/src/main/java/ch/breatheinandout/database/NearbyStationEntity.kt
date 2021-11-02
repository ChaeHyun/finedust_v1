package ch.breatheinandout.database

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.nearbystation.model.NearbyStation
import javax.inject.Inject

data class NearbyStationEntity(
    val stationName: String,
    val stationAddr: String,
    val distanceKm: String
)

class NearbyStationEntityMapper @Inject constructor(): DataMapper<NearbyStationEntity, NearbyStation> {
    override fun mapToDomainModel(inputModel: NearbyStationEntity): NearbyStation {
        return NearbyStation(
            inputModel.stationName,
            inputModel.stationAddr,
            inputModel.distanceKm
        )
    }

    override fun mapFromDomainModel(domainModel: NearbyStation): NearbyStationEntity {
        return NearbyStationEntity(
            domainModel.stationName,
            domainModel.stationAddressLine,
            domainModel.distance
        )
    }
}