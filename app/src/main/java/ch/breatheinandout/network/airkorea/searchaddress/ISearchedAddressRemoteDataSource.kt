package ch.breatheinandout.network.airkorea.searchaddress

import ch.breatheinandout.domain.searchaddress.model.SearchedAddress

interface ISearchedAddressRemoteDataSource {
    suspend fun search(umdName: String) : List<SearchedAddress>
}