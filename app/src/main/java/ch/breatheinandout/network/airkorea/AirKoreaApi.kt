package ch.breatheinandout.network.airkorea

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApi {
    companion object {
        const val AIRKOREA_API_KEY = "eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D"
        const val AIRKOREA_API_KEY_WRONG = "Mmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D"
    }

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList?")
    suspend fun getNearbyStationListByTmCoordinates(
        @Query("tmX") longitudeX: String,
        @Query("tmY") latitudeY: String,
        @Query("returnType") returnType: String = "json",
        @Query("serviceKey") serviceKey: String = AIRKOREA_API_KEY
    ) : Response<List<NearbyStationDto>>

}
