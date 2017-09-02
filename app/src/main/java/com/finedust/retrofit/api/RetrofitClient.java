package com.finedust.retrofit.api;

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

}
