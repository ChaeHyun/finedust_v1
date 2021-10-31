package ch.breatheinandout.location.data

import android.location.Location
import ch.breatheinandout.common.mapper.DataMapper
import javax.inject.Inject

class CoordinatesMapper @Inject constructor() : DataMapper<Location, Coordinates> {
    override fun mapToDomainModel(data: Location): Coordinates {
        return Coordinates(data.longitude.toString(), data.latitude.toString())
    }

    override fun mapFromDomainModel(domain: Coordinates): Location {
        TODO("Not yet implemented")
    }
}
