package ch.breatheinandout.network

import javax.inject.Inject

class UrlProvider @Inject constructor(){
    companion object {
        const val AIRKOREA_BASE_URL = "http://apis.data.go.kr/B552584/"
        const val KAKAO_BASE_URL = "https://dapi.kakao.com/v2/"
    }

    fun baseKakaoUrl() : String = KAKAO_BASE_URL
    fun baseAirkoreaUrl() : String = AIRKOREA_BASE_URL


    fun getQueryParamsForTransCoords(wgsX: String, wgsY: String): Map<String, String> {
        return HashMap<String, String>().apply {
            put("x", wgsX)
            put("y", wgsY)
        }
    }

}