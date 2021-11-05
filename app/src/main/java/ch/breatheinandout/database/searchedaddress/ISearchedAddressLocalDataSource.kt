package ch.breatheinandout.database.searchedaddress

import ch.breatheinandout.domain.searchaddress.SearchedAddress

interface ISearchedAddressLocalDataSource {
    suspend fun save(item: SearchedAddress)
    suspend fun read(): List<SearchedAddress>
    suspend fun delete(item: SearchedAddress)
    suspend fun dropTable()
}