package ch.breatheinandout.dependencyinjection

import android.content.Context
import androidx.room.Room
import ch.breatheinandout.database.Database
import ch.breatheinandout.database.locationandstation.LocationAndStationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @AppScoped
    @Provides
    fun createDatabase(context: Context) : Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Database.DB_NAME
        ).build()
    }

    @AppScoped
    @Provides
    fun locationAndStationDao(database: Database): LocationAndStationDao = database.locationAndStationDao()

}