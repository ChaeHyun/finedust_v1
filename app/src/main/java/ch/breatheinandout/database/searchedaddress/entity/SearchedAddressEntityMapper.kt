package ch.breatheinandout.database.searchedaddress.entity

import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import javax.inject.Inject

class SearchedAddressEntityMapper @Inject constructor():
    DataMapper<SearchedAddressEntity, SearchedAddress> {
    override fun mapToDomainModel(data: SearchedAddressEntity): SearchedAddress {
        return SearchedAddress(
            AddressLine(data.addr, data.sidoName, data.sggName, data.umdName),
            Coordinates(data.tmX, data.tmY)
        )
    }

    override fun mapFromDomainModel(domain: SearchedAddress): SearchedAddressEntity {
        val addrLine = domain.addressLine
        val coords = domain.tmCoords
        return SearchedAddressEntity(
            addrLine.addr, addrLine.sidoName, addrLine.sggName, addrLine.umdName,
            coords.longitudeX, coords.latitudeY
        )
    }

    fun mapToDomainList(list: List<SearchedAddressEntity>) : List<SearchedAddress> {
        return list.map { mapToDomainModel(it) }
    }
}