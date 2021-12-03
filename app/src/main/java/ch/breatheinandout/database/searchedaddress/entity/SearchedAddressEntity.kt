package ch.breatheinandout.database.searchedaddress.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.breatheinandout.common.mapper.DataMapper
import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import javax.inject.Inject

@Entity(tableName = "SearchedAddressEntity")
data class SearchedAddressEntity(
    @PrimaryKey
    val addr: String,
    val sidoName: String,
    val sggName: String,
    val umdName: String,
    val tmX: String,
    val tmY: String
)

