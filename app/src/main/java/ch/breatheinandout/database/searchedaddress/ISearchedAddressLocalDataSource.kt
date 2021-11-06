package ch.breatheinandout.database.searchedaddress

import ch.breatheinandout.domain.searchaddress.model.SearchedAddress

interface ISearchedAddressLocalDataSource {
    suspend fun save(item: SearchedAddress)
    suspend fun read(): List<SearchedAddress>
    suspend fun delete(item: SearchedAddress)
    suspend fun dropTable()
}