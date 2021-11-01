package ch.breatheinandout.network.airkorea

import ch.breatheinandout.NearbyStation
import ch.breatheinandout.common.mapper.DataMapper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NearbyStationDto(
    @SerializedName("tm")
    @Expose
    val tm: String,
    @SerializedName("addr")
    @Expose
    val stationAddr: String,
    @SerializedName("stationName")
    @Expose
    val stationName: String
) : DataMapper<NearbyStationDto, NearbyStation> {
    override fun mapToDomainModel(data: NearbyStationDto): NearbyStation {
        TODO("Not yet implemented")
    }

    override fun mapFromDomainModel(domain: NearbyStation): NearbyStationDto {
        TODO("Not yet implemented")
    }
}