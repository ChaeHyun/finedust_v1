package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.network.airkorea.searchaddress.ISearchedAddressRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(
    private val remoteSource : ISearchedAddressRemoteDataSource
) {
    sealed class Result {
        data class Success(val searchedAddressList: List<SearchedAddress>) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun search(umdName: String): Result {
        return try {
            val addresses: List<SearchedAddress> = remoteSource.search(umdName)
            Logger.i("check(addresses) -> $addresses")
            addresses.isEmpty().also {
                Result.Failure("Response is empty", NullPointerException())
            }
            Result.Success(addresses)
        } catch (e: Exception) {
            Result.Failure("Network Failed", e)
        }
    }
}