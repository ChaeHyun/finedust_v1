package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.database.searchedaddress.ISearchedAddressLocalDataSource
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadSearchedAddressUseCase @Inject constructor(
    private val localSource: ISearchedAddressLocalDataSource
) {
    suspend fun read() : List<SearchedAddress> = withContext(Dispatchers.IO) {
        try {
            return@withContext localSource.read()
        } catch (e: Exception) {
            Logger.e("Database failed : ${e.message}")
            return@withContext emptyList()
        }
    }
}