package ch.breatheinandout.network.airkorea.searchaddress

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.location.model.address.AddressLine
import ch.breatheinandout.location.model.coordinates.Coordinates
import ch.breatheinandout.searchaddress.SearchedAddress
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class SearchedAddressDto(
    @SerializedName("umdName")
    @Expose
    val umdName: String,
    @SerializedName("sggName")
    @Expose
    val sggName: String,
    @SerializedName("sidoName")
    @Expose
    val sidoName: String,

    @SerializedName("tmX")
    @Expose
    val longTmX: String,            // Longitude of the address.
    @SerializedName("tmY")
    @Expose
    val latTmY: String              // Latitude of the address.
)

class SearchedAddressDtoMapper @Inject constructor(): DataMapper<SearchedAddressDto, SearchedAddress> {
    override fun mapToDomainModel(data: SearchedAddressDto): SearchedAddress {
        val address = data.sidoName.plus(" " + data.sggName + " " + data.umdName)

        return SearchedAddress(
            AddressLine(address, data.sidoName, data.sggName, data.umdName),
            Coordinates(data.longTmX, data.latTmY)
        )
    }

    override fun mapFromDomainModel(domain: SearchedAddress): SearchedAddressDto {
        throw IllegalStateException("it's not supported.")
    }

    fun mapToDomainList(list: List<SearchedAddressDto>): List<SearchedAddress> {
        return list.map { item -> mapToDomainModel(item) }
    }
}