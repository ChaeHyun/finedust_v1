package ch.breatheinandout.retrofit.api;

import ch.breatheinandout.model.AirConditionList;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.model.AddressList;
import ch.breatheinandout.model.ForecastList;
import ch.breatheinandout.model.GpsData;
import ch.breatheinandout.model.StationList;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

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
