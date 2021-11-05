package ch.breatheinandout.database.locationandstation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.breatheinandout.domain.location.model.LocationWithNearbyStation
import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import javax.inject.Inject

@Entity(tableName = "LocationAndStationEntity")
data class LocationAndStationEntity(
    @PrimaryKey
    val addr: String,
    val sidoName: String,
    val sggName: String,
    val umdName: String,
    val tmX: String,
    val tmY: String,
    @Embedded
    val nearbyStationEntity: NearbyStationEntity
)

class LocationAndStationEntityMapper @Inject constructor(val mapper: NearbyStationEntityMapper): DataMapper<LocationAndStationEntity, LocationWithNearbyStation> {
    override fun mapToDomainModel(data: LocationAndStationEntity): LocationWithNearbyStation {
        return LocationWithNearbyStation(
            LocationPoint(
                AddressLine(data.addr, data.sidoName, data.sggName, data.umdName),
                Coordinates(data.tmX, data.tmY),
                wgsCoords = null
            ),
            NearbyStationEntityMapper().mapToDomainModel(data.nearbyStationEntity)
        )
    }

    override fun mapFromDomainModel(domain: LocationWithNearbyStation): LocationAndStationEntity {
        val addressLine = domain.locationPoint.addressLine
        val tmCoords = domain.locationPoint.tmCoords
        val nearbyStation = domain.nearbyStation
        return LocationAndStationEntity(
            addressLine.addr, addressLine.sidoName, addressLine.sggName, addressLine.umdName,
            tmCoords.longitudeX, tmCoords.latitudeY,
            mapper.mapFromDomainModel(nearbyStation)
        )
    }
}