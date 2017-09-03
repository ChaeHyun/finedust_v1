package com.finedust.retrofit.api;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public static Map<String, String> setQueryParams(String stationName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataTerm", "daily");
        queryParams.put("pageNo" , "1");
        queryParams.put("numOfRows" , "10");
        queryParams.put("ver" , "1.1");
        queryParams.put("_returnType" , "json");
        queryParams.put("stationName" , stationName);

        return queryParams;
    }

}
