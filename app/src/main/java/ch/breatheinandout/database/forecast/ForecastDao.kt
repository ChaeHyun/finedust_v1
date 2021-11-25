package ch.breatheinandout.database.forecast

import androidx.room.*
import ch.breatheinandout.database.converters.MapTypeConverter
import java.util.*

@Dao
@TypeConverters(MapTypeConverter::class)
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<ForecastEntity>)

    @Query("SELECT * FROM ForecastEntity WHERE dataTime = :dataTime ORDER BY dataTime DESC")
    fun getForecast(dataTime: Date) : List<ForecastEntity>

    @Query("SELECT * FROM ForecastEntity WHERE informCode = :informCode AND dataTime = :dataTime ORDER BY dataTime DESC")
    fun getForecast(dataTime: Date, informCode: String) : List<ForecastEntity>

    @Update
    fun update(entity: ForecastEntity)

    @Delete
    fun delete(entity: ForecastEntity)

    @Query("DELETE FROM ForecastEntity")
    fun dropTable()
}