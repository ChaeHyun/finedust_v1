package com.finedust.retrofit.api;

import com.finedust.model.Const;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CH on 2017-09-02.
 */

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static final String BASE_URL = "http://openapi.airkorea.or.kr/openapi/services/rest/";

    /**
     * Get Retrofit Instance
     * */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    public static Map<String, String> setQueryParamsForStationName(String stationName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataTerm", "daily");
        queryParams.put("pageNo" , "1");
        queryParams.put("numOfRows" , "10");
        queryParams.put("ver" , "1.1");
        queryParams.put("_returnType" , "json");
        queryParams.put("stationName" , stationName);

        return queryParams;
    }

    public static Map<String, String> setQueryParamsForAddress(String umdName) {
        Map<String, String> queryParmas = new HashMap<>();
        queryParmas.put("_returnType", "json");
        queryParmas.put("pageNo", "1");
        queryParmas.put("numOfRows", "50");
        queryParmas.put("umdName",umdName);

        return queryParmas;
    }

    public static String getGpsConvertUrl(String y, String x) {
        return "https://apis.daum.net/local/geo/transcoord"
                + "?fromCoord=wgs84" + "&toCoord=TM" + "&output=json"
                + "&x=" + x  + "&y=" + y
                + "&apikey=" + Const.DAUM_API_KEY ;
    }

}
