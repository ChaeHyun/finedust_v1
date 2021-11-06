package ch.breatheinandout.network.airkorea.searchaddress

import ch.breatheinandout.network.airkorea.AirKoreaApi
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import javax.inject.Inject

class SearchedAddressRemoteDataSource @Inject constructor(
    private val airKoreaApi: AirKoreaApi,
    private val mapper: SearchedAddressDtoMapper
) : ISearchedAddressRemoteDataSource {

    override suspend fun search(umdName: String): List<SearchedAddress> {
        try {
            val response = airKoreaApi.searchAddressByItsName(umdName)
            if (response.isSuccessful) {
                val addresses: List<SearchedAddressDto> = response.body()!!
                if (addresses.isNotEmpty())
                    return mapper.mapToDomainList(addresses)
            }
            return emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
}