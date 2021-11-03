package ch.breatheinandout.dependencyinjection

import ch.breatheinandout.dependencyinjection.qualifier.RetrofitForAirKorea
import ch.breatheinandout.dependencyinjection.qualifier.RetrofitForKakao
import ch.breatheinandout.network.UrlProvider
import ch.breatheinandout.network.airkorea.AirKoreaApi
import ch.breatheinandout.network.airkorea.AirKoreaResponseFilteringInterceptor
import ch.breatheinandout.network.transcoords.KakaoApi
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
object NetworkModule {
    @RetrofitForKakao
    @AppScoped
    @Provides
    fun retrofitKakao(
        urlProvider: UrlProvider,
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor
    ): Retrofit {
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
    fun airkoreaApi(@RetrofitForAirKorea retrofit: Retrofit) : AirKoreaApi = retrofit.create(
        AirKoreaApi::class.java)

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
}