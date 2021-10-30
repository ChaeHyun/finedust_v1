package ch.breatheinandout.dependencyinjection

import android.app.Application
import android.content.Context
import android.location.LocationManager
import ch.breatheinandout.common.LocationHandler
import ch.breatheinandout.location.data.CoordinatesMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

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