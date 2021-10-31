package ch.breatheinandout.network.transcoords

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface KakaoApi {
    companion object {
        const val KAKAO_API_KEY = "2cc512a937a56a6060f41dfbf1a2f775"
    }

    @Headers("Authorization: KakaoAK $KAKAO_API_KEY")
    @GET("local/geo/transcoord.json?input_coord=WGS84&output_coord=TM")
    suspend fun convertWgsToTmCoordinates(
        @QueryMap params: Map<String, String>
    ) : Response<KakaoCoordsResponse>
}
