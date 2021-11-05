package ch.breatheinandout.domain.airquality

import ch.breatheinandout.network.airkorea.airquality.IAirQualityRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetAirQualityDataUseCase @Inject constructor(
    private val remoteDataSource: IAirQualityRemoteDataSource
) {
    sealed class Result {
        data class Success(val airQuality: AirQuality) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }
    private val className = GetAirQualityDataUseCase::class.simpleName

    suspend fun get(stationName: String) : Result {
        return try {
            val airQuality: AirQuality? = remoteDataSource.getAirQualityData(stationName)
            airQuality?.let { Result.Success(it) } ?: Result.Failure("Response is empty.", NullPointerException())
        } catch (e: Exception) {
            Logger.e("Network failed at $className")
            Result.Failure("Network failed a $className", e)
        }
    }
}