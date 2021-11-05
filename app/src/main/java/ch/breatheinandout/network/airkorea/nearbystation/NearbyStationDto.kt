package ch.breatheinandout.network.airkorea.nearbystation

import ch.breatheinandout.domain.nearbystation.model.NearbyStation
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
) {
    fun mapToDomain() : NearbyStation = NearbyStation(stationName, stationAddr, tm)
}