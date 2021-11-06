package ch.breatheinandout.database.airquality


import ch.breatheinandout.domain.airquality.model.AirQuality
import javax.inject.Inject

class AirQualityLocalDataSource @Inject constructor(
    private val dao: AirQualityDao
) : IAirQualityLocalDataSource {

    override suspend fun get(stationName: String) : AirQuality? {
        try {
            val mapper = AirQualityEntityMapper(stationName)
            val entity = dao.getAirQualityData(stationName)
            return entity?.let { mapper.mapToDomainModel(it) }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun save(stationName: String, data: AirQuality) {
        try {
            val mapper = AirQualityEntityMapper(stationName)
            val entity = mapper.mapFromDomainModel(data)
            dao.insert(entity)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun save(stationName: String, list: List<AirQuality>) {
        try {
            val mapper = AirQualityEntityMapper(stationName)
            val entities = mapper.mapToListEntity(list)
            dao.insert(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun update(stationName: String, data: AirQuality) {
        try {
            val mapper = AirQualityEntityMapper(stationName)
            val entity = mapper.mapFromDomainModel(data)
            dao.update(entity)
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