package com.finedust.retrofit.api;

import com.finedust.model.AirConditionList;
import com.finedust.model.Const;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    /**
     * Service Name : MsrstnInfoInqireSvc
     * Operation Method : getNearbyMsrstnList
     * Parameters : tmX, tmY, _returnType, ServiceKey
     * */

    /**
     * Service Name : ArpltnInforInqireSvc
     * Operation Method : getMsrstnAcctoRltmMesureDnsty
     * Parameters : stationName, dataTerm, pageNo, numOfRows, ServiceKey, ver, _returnType
     * */

    @GET("ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?ServiceKey="+ Const.SERVICEKEY)
    Call<AirConditionList> getAirConditionData(
            @QueryMap Map<String, String> params
            //@Query(value = "ServiceKey") List<String> serviceKey
    );

    @GET
    public Call<AirConditionList> getAirConditionInfo(@Url String url);

}
