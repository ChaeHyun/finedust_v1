package ch.breatheinandout.dependencyinjection

import android.app.Application
import android.content.Context
import android.location.LocationManager
import ch.breatheinandout.database.locationandstation.ILocationLocalDataSource
import ch.breatheinandout.database.locationandstation.LocationLocalDataSource
import ch.breatheinandout.location.provider.LocationHandler
import ch.breatheinandout.location.model.coordinates.CoordinatesMapper
import ch.breatheinandout.network.airkorea.nearbystation.INearbyStationRemoteDataSource
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationRemoteDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }

    @AppScoped
    @Provides
    fun locationHandler(fusedLocationProvider: FusedLocationProviderClient, mapper: CoordinatesMapper): LocationHandler = LocationHandler(fusedLocationProvider, mapper)

    @AppScoped
    @Provides
    fun locationManager(context: Context): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @AppScoped
    @Provides
    fun fusedLocationProviderClient(context: Context) : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @AppScoped
    @Provides
    fun coordinatesMapper(): CoordinatesMapper = CoordinatesMapper()
}

@Module
@InstallIn(SingletonComponent::class)
@SuppressWarnings()
abstract class DataSourceModule {
    @Binds
    abstract fun bindNearbyStationRemoteDataSource(sourceImpl: NearbyStationRemoteDataSource) : INearbyStationRemoteDataSource

    @Binds
    abstract fun bindLocationLocalDataSource(sourceImpl: LocationLocalDataSource): ILocationLocalDataSource
}
