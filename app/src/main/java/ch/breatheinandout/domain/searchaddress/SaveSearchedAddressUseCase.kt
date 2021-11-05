package ch.breatheinandout.domain.searchaddress

import ch.breatheinandout.database.searchedaddress.ISearchedAddressLocalDataSource
import com.orhanobut.logger.Logger
import java.lang.Exception
import javax.inject.Inject

class SaveSearchedAddressUseCase @Inject constructor(
    private val localSource: ISearchedAddressLocalDataSource
) {

    suspend fun save(searchedAddress: SearchedAddress) {
        try {
            localSource.save(searchedAddress)
        } catch (e: Exception) {
            Logger.e("Error -> $e")
        }
    }
}