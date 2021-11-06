package ch.breatheinandout.database.airquality

import androidx.room.*

@Dao
interface AirQualityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airQualityEntity: AirQualityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airQualityEntities: List<AirQualityEntity>)

    @Query("SELECT * FROM AirQualityEntity WHERE stationName = :stationName ORDER BY dataTime DESC")
    fun getAllAirQualityData(stationName: String) : List<AirQualityEntity>

    @Query("SELECT * FROM AirQualityEntity WHERE stationName = :stationName ORDER BY dataTime DESC")
    fun getAirQualityData(stationName: String) : AirQualityEntity?

    @Update
    fun update(airQualityEntity: AirQualityEntity)

    @Delete
    fun delete(airQualityEntity: AirQualityEntity)

    @Query("DELETE FROM AirQualityEntity")
    fun dropTable()
}