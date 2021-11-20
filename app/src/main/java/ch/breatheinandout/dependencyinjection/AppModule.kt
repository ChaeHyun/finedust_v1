package ch.breatheinandout.dependencyinjection

import android.app.Application
import android.content.Context
import android.location.LocationManager
import ch.breatheinandout.common.utils.PreferencesUtils
import ch.breatheinandout.database.airquality.AirQualityLocalDataSource
import ch.breatheinandout.database.airquality.IAirQualityLocalDataSource
import ch.breatheinandout.database.locationandstation.ILocationLocalDataSource
import ch.breatheinandout.database.locationandstation.LocationLocalDataSource
import ch.breatheinandout.database.searchedaddress.ISearchedAddressLocalDataSource
import ch.breatheinandout.database.searchedaddress.SearchedAddressLocalDataSource
import ch.breatheinandout.domain.location.provider.LocationHandler
import ch.breatheinandout.domain.location.model.coordinates.CoordinatesMapper
import ch.breatheinandout.network.airkorea.airquality.AirQualityRemoteDataSource
import ch.breatheinandout.network.airkorea.airquality.IAirQualityRemoteDataSource
import ch.breatheinandout.network.airkorea.forecast.ForecastRemoteDataSource
import ch.breatheinandout.network.airkorea.forecast.IForecastRemoteDataSource
import ch.breatheinandout.network.airkorea.nearbystation.INearbyStationRemoteDataSource
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationRemoteDataSource
import ch.breatheinandout.network.airkorea.searchaddress.ISearchedAddressRemoteDataSource
import ch.breatheinandout.network.airkorea.searchaddress.SearchedAddressRemoteDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
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

    @AppScoped
    @Provides
    fun gson(): Gson = Gson().newBuilder().setPrettyPrinting().create()

    @AppScoped
    @Provides
    fun preferencesUtils(context: Context, gson: Gson) : PreferencesUtils = PreferencesUtils(context, gson)
}

@Module
@InstallIn(SingletonComponent::class)
@SuppressWarnings()
abstract class DataSourceModule {
    @Binds
    abstract fun bindNearbyStationRemoteDataSource(sourceImpl: NearbyStationRemoteDataSource) : INearbyStationRemoteDataSource

    @Binds
    abstract fun bindLocationLocalDataSource(sourceImpl: LocationLocalDataSource): ILocationLocalDataSource

    @Binds
    abstract fun bindSearchedAddressRemoteDataSource(sourceImpl: SearchedAddressRemoteDataSource) : ISearchedAddressRemoteDataSource

    @Binds
    abstract fun bindSearchedAddressLocalDataSource(sourceImpl: SearchedAddressLocalDataSource) : ISearchedAddressLocalDataSource

    @Binds
    abstract fun bindAirQualityRemoteDataSource(sourceImpl: AirQualityRemoteDataSource) : IAirQualityRemoteDataSource

    @Binds
    abstract fun bindAirQualityLocalDataSource(sourceImpl: AirQualityLocalDataSource) : IAirQualityLocalDataSource

    @Binds
    abstract fun bindForecastRemoteDataSource(sourceImpl: ForecastRemoteDataSource) : IForecastRemoteDataSource
}
