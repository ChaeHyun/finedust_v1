package ch.breatheinandout.domain.location

import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import ch.breatheinandout.network.UrlProvider
import ch.breatheinandout.network.transcoords.CoordsDtoMapper
import ch.breatheinandout.network.transcoords.KakaoApi
import ch.breatheinandout.network.transcoords.KakaoCoordsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.NullPointerException

/**
 * It translates WGS coordinates to Tm Coordinates.
 * */
class TransCoordinatesUseCase @Inject constructor(
    private val kakaoApi: KakaoApi,
    private val urlProvider: UrlProvider
) {
    sealed class Result {
        data class Success(val tmCoords: Coordinates) : Result()
        data class Failure(val message: String, val cause: Throwable) : Result()
    }

    suspend fun translateWgsToTmCoordinates(wgsCoordinates: Coordinates) : Result {
        val queryParams = urlProvider.getQueryParamsForTransCoords(wgsCoordinates.longitudeX, wgsCoordinates.latitudeY)
        return try {
            withContext(Dispatchers.IO) {
                val response = kakaoApi.convertWgsToTmCoordinates(queryParams)
                if (response.isSuccessful) {
                    val kakaoCoordsResponse: KakaoCoordsResponse = response.body() as KakaoCoordsResponse
                    val dto = kakaoCoordsResponse.documents[0]

                    Result.Success(CoordsDtoMapper().mapToDomainModel(dto))
                } else {
                    Result.Failure("[HTTP.Unsuccessful] Failed to convert WGS84 to TM Coordinates.", NullPointerException())
                }
            }
        } catch (e: Exception) {
            Result.Failure("[Failure] Failed to translate WGS84 to TM Coordinates", e)
        }
    }
}