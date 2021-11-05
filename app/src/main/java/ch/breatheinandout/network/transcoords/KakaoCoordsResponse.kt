package ch.breatheinandout.network.transcoords

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

/**
 * This is a network DTO to store converted coordinates by Kakao Api server.
 * */
data class KakaoCoordsResponse(
    @SerializedName("documents")
    @Expose
    val documents: List<CoordsDto>
)

data class CoordsDto(
    @SerializedName("x")
    @Expose val tm_x: String = "0",
    @SerializedName("y")
    @Expose val tm_y: String = "0",
    val wgs_x: String = "0",
    val wgs_y: String = "0",
)

class CoordsDtoMapper @Inject constructor() : DataMapper<CoordsDto, Coordinates> {
    override fun mapToDomainModel(data: CoordsDto): Coordinates {
        return Coordinates(data.tm_x, data.tm_y)
    }

    override fun mapFromDomainModel(domain: Coordinates): CoordsDto {
        TODO("Not yet implemented")
    }
}