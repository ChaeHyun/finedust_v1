package ch.breatheinandout.location

import android.location.Location
import ch.breatheinandout.common.mapper.DataMapper
import javax.inject.Inject


class LocationPointMapper @Inject constructor() : DataMapper<Location, LocationPoint>{
    override fun mapToDomainModel(data: Location): LocationPoint {
        return LocationPoint(data.longitude.toString(), data.latitude.toString())
    }

    override fun mapFromDomainModel(domain: LocationPoint): Location {
        TODO("Not yet implemented")
    }
}