package ch.breatheinandout.dependencyinjection

import android.app.Application
import android.content.Context
import android.location.LocationManager
import ch.breatheinandout.dependencyinjection.qualifier.RetrofitForAirKorea
import ch.breatheinandout.dependencyinjection.qualifier.RetrofitForKakao
import ch.breatheinandout.location.provider.LocationHandler
import ch.breatheinandout.location.model.coordinates.CoordinatesMapper
import ch.breatheinandout.network.transcoords.KakaoApi
import ch.breatheinandout.network.UrlProvider
import ch.breatheinandout.network.airkorea.AirKoreaApi
import ch.breatheinandout.network.airkorea.AirKoreaResponseFilteringInterceptor
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationDataSource
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationDataSourceImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

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

    // ------ Network Module ------
    @RetrofitForKakao
    @AppScoped
    @Provides
    fun retrofitKakao(urlProvider: UrlProvider, @Named("LoggingInterceptor") loggingInterceptor: Interceptor): Retrofit {
        val okHttp = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(urlProvider.baseKakaoUrl())
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @RetrofitForAirKorea
    @AppScoped
    @Provides
    fun retrofitAirkorea(
        urlProvider: UrlProvider,
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        @Named("AirKoreaInterceptor") airKoreaInterceptor: Interceptor
    ) : Retrofit {
        val okHttp = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(airKoreaInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(urlProvider.baseAirkoreaUrl())
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScoped
    @Provides
    fun kakaoApi(@RetrofitForKakao retrofit: Retrofit) : KakaoApi = retrofit.create(KakaoApi::class.java)

    @AppScoped
    @Provides
    fun airkoreaApi(@RetrofitForAirKorea retrofit: Retrofit) : AirKoreaApi = retrofit.create(AirKoreaApi::class.java)

    @AppScoped
    @Provides
    @Named("LoggingInterceptor")
    fun httpLoggingInterceptor() : Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @AppScoped
    @Provides
    @Named("AirKoreaInterceptor")
    fun airKoreaResponseInterceptor() : Interceptor = AirKoreaResponseFilteringInterceptor()

    @Provides
    fun nearbyStationDataSource(airKoreaApi: AirKoreaApi) : NearbyStationDataSource = NearbyStationDataSourceImpl(airKoreaApi)
}