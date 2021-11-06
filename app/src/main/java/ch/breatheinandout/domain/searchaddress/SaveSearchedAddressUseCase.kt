package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.database.searchedaddress.ISearchedAddressLocalDataSource
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SaveSearchedAddressUseCase @Inject constructor(
    private val localSource: ISearchedAddressLocalDataSource
) {

    suspend fun save(searchedAddress: SearchedAddress) = withContext(Dispatchers.IO) {
        try {
            localSource.save(searchedAddress)
        } catch (e: Exception) {
            Logger.e("Error -> $e")
        }
    }
}