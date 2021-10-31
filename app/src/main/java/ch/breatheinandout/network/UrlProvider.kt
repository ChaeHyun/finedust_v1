package ch.breatheinandout.network

import javax.inject.Inject

class UrlProvider @Inject constructor(){
    companion object {
        const val KAKAO_BASE_URL = "https://dapi.kakao.com/v2/"
    }

    fun baseUrl() : String {
        return KAKAO_BASE_URL
    }

    fun getQueryParamsForTransCoords(wgsX: String, wgsY: String): Map<String, String> {
        return HashMap<String, String>().apply {
            put("x", wgsX)
            put("y", wgsY)
        }
    }

}