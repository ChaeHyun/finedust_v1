package ch.breatheinandout.dependencyinjection

import android.app.Application
import android.content.Context
import android.location.LocationManager
import ch.breatheinandout.common.LocationHandler
import ch.breatheinandout.location.data.AndroidLocationMapper
import ch.breatheinandout.network.transcoords.KakaoApi
import ch.breatheinandout.network.UrlProvider
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
    fun locationHandler(fusedLocationProvider: FusedLocationProviderClient, mapper: AndroidLocationMapper): LocationHandler = LocationHandler(fusedLocationProvider, mapper)

    @AppScoped
    @Provides
    fun locationManager(context: Context): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @AppScoped
    @Provides
    fun fusedLocationProviderClient(context: Context) : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @AppScoped
    @Provides
    fun coordinatesMapper(): AndroidLocationMapper = AndroidLocationMapper()

    // ------ Network Module ------
    @AppScoped
    @Provides
    fun retrofit(urlProvider: UrlProvider, @Named("LoggingInterceptor") loggingInterceptor: Interceptor): Retrofit {
        val okHttp = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(urlProvider.baseUrl())
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScoped
    @Provides
    fun kakaoApi(retrofit: Retrofit) : KakaoApi = retrofit.create(KakaoApi::class.java)

    @AppScoped
    @Provides
    @Named("LoggingInterceptor")
    fun httpLoggingInterceptor() : Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}