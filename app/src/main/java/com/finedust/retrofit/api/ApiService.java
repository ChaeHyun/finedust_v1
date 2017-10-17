package com.finedust.retrofit.api;

import com.finedust.model.AirConditionList;
import com.finedust.model.Const;
import com.finedust.model.AddressList;
import com.finedust.model.ForecastList;
import com.finedust.model.GpsData;
import com.finedust.model.Station;
import com.finedust.model.StationList;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import static android.R.attr.value;

public interface ApiService {

    @GET("ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?ServiceKey=" + Const.SERVICEKEY)
    Observable<AirConditionList> getAirConditionData(
            @QueryMap Map<String, String> params
    );

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList?ServiceKey=" + Const.SERVICEKEY)
    Observable<StationList> getNearStationList(
            @Query("tmX") String x,
            @Query("tmY") String y,
            @Query("_returnType") String returnType
    );

    @GET("MsrstnInfoInqireSvc/getTMStdrCrdnt?ServiceKey=" + Const.SERVICEKEY)
    Observable<AddressList> getAddressData(
            @QueryMap Map<String, String> params
    );

    @GET
    Observable<GpsData> convertGpsData(@Url String url);

    @GET("ArpltnInforInqireSvc/getMinuDustFrcstDspth?ServiceKey=" + Const.SERVICEKEY)
    Observable<ForecastList> getForecastData(
            @QueryMap Map<String, String> params
    );

}
