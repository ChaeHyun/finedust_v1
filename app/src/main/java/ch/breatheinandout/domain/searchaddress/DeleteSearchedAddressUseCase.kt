package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.database.searchedaddress.ISearchedAddressLocalDataSource
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteSearchedAddressUseCase @Inject constructor(
    private val localSource: ISearchedAddressLocalDataSource
) {
    suspend fun delete(searchedAddress: SearchedAddress) = withContext(Dispatchers.IO) {
        try {
            localSource.delete(searchedAddress)
        } catch (e: Exception) {
            Logger.e("Error -> $e")
        }
    }
}