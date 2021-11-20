package ch.breatheinandout.domain.forecast

import ch.breatheinandout.network.airkorea.forecast.IForecastRemoteDataSource
import com.orhanobut.logger.Logger
import javax.inject.Inject

class GetForeCastUseCase @Inject constructor(
    private val remoteDataSource: IForecastRemoteDataSource
) {

    suspend fun forecast(searchDate: String) {
        val result = remoteDataSource.getForecast(searchDate)
        Logger.i("$result")
    }
}