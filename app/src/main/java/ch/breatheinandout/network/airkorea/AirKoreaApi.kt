package ch.breatheinandout.network.airkorea

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApi {
    companion object {
        const val AIRKOREA_API_KEY = "4CP4GxSL9Z%2Focyjxy9hnLj4tRji3XIJfJGhzjHEbTYIRIuITX46H3ehG82%2BwxYROixuYRriwmE65c42QHFZNDA%3D%3D"
        const val AIRKOREA_API_KEY_WRONG = "aMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D"
    }

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=$AIRKOREA_API_KEY")
    suspend fun getNearbyStationListByTmCoordinates(
        @Query("tmX") longitudeX: String,
        @Query("tmY") latitudeY: String,
        @Query("returnType") returnType: String = "json"
    ) : Response<List<NearbyStationDto>>

}
