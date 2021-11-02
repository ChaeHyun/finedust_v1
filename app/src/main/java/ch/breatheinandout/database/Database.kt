package ch.breatheinandout.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [LocationAndStationEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    companion object {
        const val DB_NAME: String = "airquality.db"
    }

    abstract fun locationAndStationDao(): LocationAndStationDao
}