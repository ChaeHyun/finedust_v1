package ch.breatheinandout.database.searchedaddress

import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import javax.inject.Inject

class SearchedAddressLocalDataSource @Inject constructor(
    private val dao: SearchedAddressDao,
    private val mapper: SearchedAddressEntityMapper
) : ISearchedAddressLocalDataSource {

    override suspend fun save(item: SearchedAddress) {
        try {
            dao.insert(mapper.mapFromDomainModel(item))
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun read() : List<SearchedAddress> {
        try {
            val entities = dao.getAll()
            return mapper.mapToDomainList(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun delete(item: SearchedAddress) {
        try {
            val target = mapper.mapFromDomainModel(item)
            dao.delete(target)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun dropTable() {
        try {
            dao.dropTable()
        } catch (e: Exception) {
            throw e
        }
    }
}