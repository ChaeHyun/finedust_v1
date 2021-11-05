package ch.breatheinandout.domain.location.model.address

import android.location.Address
import ch.breatheinandout.common.mapper.DataMapper
import javax.inject.Inject

class AddressLineMapper @Inject constructor(): DataMapper<Address, AddressLine> {
    override fun mapToDomainModel(data: Address): AddressLine {
        val sidoName = data.adminArea ?: ""
        val sggName = if (data.locality != null && data.subLocality != null) {
            data.locality.plus(" ").plus(data.subLocality)
        } else {
            (data.locality ?: "") + (data.subLocality ?: "")
        }
        val umdName = data.thoroughfare ?: ""
        val addressLine = sidoName.plus(" $sggName $umdName")
        return AddressLine(addressLine, sidoName, sggName, umdName)
    }

    override fun mapFromDomainModel(domain: AddressLine): Address {
        TODO("Not yet implemented")
    }
}