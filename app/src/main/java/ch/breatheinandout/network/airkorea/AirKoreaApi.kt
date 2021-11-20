package ch.breatheinandout.network.airkorea

import ch.breatheinandout.network.airkorea.airquality.AirQualityDto
import ch.breatheinandout.network.airkorea.forecast.ForecastDto
import ch.breatheinandout.network.airkorea.nearbystation.NearbyStationDto
import ch.breatheinandout.network.airkorea.searchaddress.SearchedAddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApi {
    companion object {
        const val AIRKOREA_API_KEY = "4CP4GxSL9Z%2Focyjxy9hnLj4tRji3XIJfJGhzjHEbTYIRIuITX46H3ehG82%2BwxYROixuYRriwmE65c42QHFZNDA%3D%3D"
        const val AIRKOREA_API_KEY_WRONG = "aMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D"

        const val RETURN_TYPE_JSON = "json"
        const val VER_FORECAST = "1.1"
        const val VER_AIRQUALITY = "1.3"
    }

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=$AIRKOREA_API_KEY")
    suspend fun getNearbyStationListByTmCoordinates(
        @Query("tmX") longitudeX: String,
        @Query("tmY") latitudeY: String,
        @Query("returnType") returnType: String = RETURN_TYPE_JSON
    ) : Response<List<NearbyStationDto>>

    @GET("MsrstnInfoInqireSvc/getTMStdrCrdnt?serviceKey=$AIRKOREA_API_KEY")
    suspend fun searchAddressByItsName(
        @Query("umdName") umdName: String,
        @Query("pageNo") pageNo: String = "1",
        @Query("numOfRows") numOfRows: String = "30",
        @Query("returnType") returnType: String = RETURN_TYPE_JSON
    ) : Response<List<SearchedAddressDto>>

    @GET("ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey=$AIRKOREA_API_KEY")
    suspend fun getAirQualityInRealTime(
        @Query("stationName") stationName: String,
        @Query("dataTerm") dataTerm: String = "daily",
        @Query("pageNo") pageNo: String = "1",
        @Query("numOfRows") numOfRows: String = "3",
        @Query("ver") version: String = VER_AIRQUALITY,
        @Query("returnType") returnType: String = RETURN_TYPE_JSON
    ) : Response<List<AirQualityDto>>

    @GET("ArpltnInforInqireSvc/getMinuDustFrcstDspth?serviceKey=$AIRKOREA_API_KEY")
    suspend fun getForecastData(
        @Query("searchDate") searchDate: String,
        @Query("returnType") returnType: String = RETURN_TYPE_JSON,
        @Query("ver") ver: String = VER_FORECAST
    ) : Response<List<ForecastDto>>
}
