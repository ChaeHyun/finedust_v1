package ch.breatheinandout.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.breatheinandout.database.airquality.AirQualityDao
import ch.breatheinandout.database.airquality.AirQualityEntity
import ch.breatheinandout.database.locationandstation.LocationAndStationDao
import ch.breatheinandout.database.locationandstation.LocationAndStationEntity
import ch.breatheinandout.database.searchedaddress.SearchedAddressDao
import ch.breatheinandout.database.searchedaddress.SearchedAddressEntity


@Database(entities = [LocationAndStationEntity::class, SearchedAddressEntity::class, AirQualityEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class Database : RoomDatabase() {
    companion object {
        const val DB_NAME: String = "airquality.db"
    }

    abstract fun locationAndStationDao(): LocationAndStationDao
    abstract fun searchedAddressDao(): SearchedAddressDao
    abstract fun airQualityDao(): AirQualityDao
}