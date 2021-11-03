package ch.breatheinandout.database.locationandstation

import androidx.room.*

@Dao
interface LocationAndStationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: LocationAndStationEntity)

    @Query("SELECT * FROM LocationAndStationEntity")
    fun getAllLocationAndStationEntity() : List<LocationAndStationEntity>

    @Query("SELECT * FROM LocationAndStationEntity WHERE umdName = :umdName AND sidoName = :sidoName")
    fun getLocationAndStationEntity(sidoName: String, umdName: String) : LocationAndStationEntity?

    @Update
    fun update(entity: LocationAndStationEntity)

    @Delete
    fun delete(entity: LocationAndStationEntity)

    @Query("DELETE FROM LocationAndStationEntity")
    fun dropTable()
}