package ch.breatheinandout.database.searchedaddress

import ch.breatheinandout.domain.searchaddress.SearchedAddress
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchedAddressLocalDataSource @Inject constructor(
    private val dao: SearchedAddressDao,
    private val mapper: SearchedAddressEntityMapper
) : ISearchedAddressLocalDataSource {

    override suspend fun save(item: SearchedAddress) = withContext(Dispatchers.IO) {
        try {
            dao.insert(mapper.mapFromDomainModel(item))
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun read() : List<SearchedAddress> = withContext(Dispatchers.IO) {
        try {
            val entities = dao.getAll()
            return@withContext mapper.mapToDomainList(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun delete(item: SearchedAddress) = withContext(Dispatchers.IO) {
        try {
            val target = mapper.mapFromDomainModel(item)
            dao.delete(target)
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun dropTable() = withContext(Dispatchers.IO){
        dao.dropTable()
    }
}