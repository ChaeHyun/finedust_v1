package com.finedust.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finedust.model.Addresses;
import com.finedust.model.AirCondition;
import com.finedust.model.AirConditionList;
import com.finedust.model.Const;
import com.finedust.model.GpsData;
import com.finedust.model.RecentData;
import com.finedust.model.StationList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.MainActivity;
import com.finedust.view.Views;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AirConditionFragmentPresenter
        implements Presenter.AirConditionFragmentPresenter,
                GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener,
                com.google.android.gms.location.LocationListener {
    private static final String TAG = AirConditionFragmentPresenter.class.getSimpleName();
    private Views.AirConditionFragmentView view;
    private Context context;

    private SharedPreferences pref;

    private GoogleApiClient googleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private RecentData mRecent;

    public AirConditionFragmentPresenter(Views.AirConditionFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        mRecent = new RecentData();
        mRecent.setAddr(new Addresses());
        pref = new SharedPreferences(context);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()\nService_disconnec tLocation");
        disconnectLocationService();

        try {
            RecentData data = (RecentData) pref.getObject(SharedPreferences.RECENT_DATA[convertModeToInteger(pref.getValue(SharedPreferences.CURRENT_MODE, Const.MODE[0]))], Const.EMPTY_STRING, new RecentData());
            Log.v(TAG , "[ RCENT_DATA from SharedPreferences ]"
                    + "\n현재모드: " +  data.getCurrentMode()
                    + "\n저장된 주소 : " + data.getAddr().getAddr()
                    + "\n동이름 : " + data.getAddr().getUmdName() + "  , 좌표 : " + data.getAddr().getTmX() + " , " + data.getAddr().getTmY()
                    + "\n측정소[1] : " + data.getSavedStations().get(0).getAddr() + " , " + data.getSavedStations().get(0).getStationName() + " , " + data.getSavedStations().get(0).getTm()
                    + "\n측정소[2] : " + data.getSavedStations().get(1).getAddr() + " , " + data.getSavedStations().get(1).getStationName() + " , " + data.getSavedStations().get(1).getTm()
                    + "\n측정소[3] : " + data.getSavedStations().get(2).getAddr() + " , " + data.getSavedStations().get(2).getStationName() + " , " + data.getSavedStations().get(2).getTm()
                    + "\n시간 : " + data.getAirCondition().get(0).getDataTime()
                    + "\nKhai : " + data.getAirCondition().get(0).getKhaiValue()
                    + "  ||  PM10 : " + data.getAirCondition().get(0).getPm10Value()
                    + "  ||  PM2.5 : " + data.getAirCondition().get(0).getPm25Value()
                    + "  ||  O3 : " + data.getAirCondition().get(0).getO3Value()
                    + "  ||  NO2 : " + data.getAirCondition().get(0).getNo2Value()
                    + "  ||  CO : " + data.getAirCondition().get(0).getCoValue()
                    + "  ||  SO2 : " + data.getAirCondition().get(0).getSo2Value()
            );
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void checkCurrentMode(String mode) {
        mRecent.setCurrentMode(mode);
        int num = convertModeToInteger(mode);
        if ( !updateWithRecentData(mode) ) {
            if ( num == 1 ) {
                Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                getNearStationList(data.getTmX(), data.getTmY());
            }
            else if ( num == 2 ) {
                Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                getNearStationList(data.getTmX(), data.getTmY());
            }
            else if ( num == 3 ) {
                Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                getNearStationList(data.getTmX(), data.getTmY());
            }
            else  {
                Log.v(TAG,"   GPS 이용해서 좌표구하기 실행" );
                getGPSCoordinates();
            }
        }


    }

    /**
     * 좌표를 파라미터로 해당 근접한 측정소 목록을 요청하는 메소드.
     * */
    @Override
    public void getNearStationList(final String x, final String y) {
        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            final Call<StationList> requestForNearStationList = apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON);
            Log.v(TAG, "Check URL(Near Station Lists) : " + apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON).request().url().toString());

            requestForNearStationList.enqueue(new Callback<StationList>() {
                @Override
                public void onResponse(Call<StationList> call, Response<StationList> response) {
                    if(response.isSuccessful()) {
                        if (response.body().getTotalCount().equals(0)) {
                            view.showSnackBarMessage("주변의 측정소를 찾지 못하였습니다.");
                        }
                        else {
                            GpsData gps = new GpsData(x, y);
                            getAirConditionData(response.body(), 0, gps);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StationList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                }
            });
        }
        else {
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);
        }
    }


    /**
     * 측정소명을 파라미터로 해당 측정소의 대기정보를 요청하는 메소드.
     * */
    @Override
    public void getAirConditionData(final StationList stationList, final int index, final GpsData gps) {
        view.showToastMessage("[ " + stationList.getList().get(index).getStationName() + " ] 측정소의 정보를 검색합니다.");
        Log.i(TAG, "검색 측정소명("+ index +") : " + stationList.getList().get(index).getStationName());

        // Checking InternetConnection
        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            String stationName = stationList.getList().get(index).getStationName();
            Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);

            Log.v(TAG, "Check URL(getAirconditionData) : " + apiService.getAirConditionData(queryParams).request().url().toString());
            final Call<AirConditionList> requestForAirConditionData = apiService.getAirConditionData(queryParams);

            requestForAirConditionData.enqueue(new Callback<AirConditionList>() {
                @Override
                public void onResponse(Call<AirConditionList> call, Response<AirConditionList> response) {
                    if(response.isSuccessful()) {
                        if (response.body().getTotalCount().equals(0)) {
                            view.showSnackBarMessage("대기오염 정보를 찾지 못하였습니다.");
                        }
                        else {
                            ArrayList<AirCondition> nextStationData = response.body().getList();

                            if (index == 0) {
                                mRecent.getAddr().setTmXTmY(gps.getTm_x(), gps.getTm_y());
                                mRecent.setSavedStations(stationList.getList());
                                mRecent.setAirCondition(nextStationData);
                            }
                            else if (index < stationList.getTotalCount()){
                                ArrayList<AirCondition> updated = updateAirConditionDataFromNextStation( nextStationData.get(0), mRecent.getAirCondition() );
                                mRecent.setAirCondition( updated );
                            }

                            // 누락된 데이터가 있으면 다음 측정소 정보 재탐색
                            if ( isAllAirDataFilled( mRecent.getAirCondition().get(0) ) || index >= (stationList.getTotalCount()-1) ) {
                                saveRecentData(mRecent);
                                Log.i(TAG, "ORDER to view : updateDataToViews");
                                view.updateDataToViews(mRecent);
                            }
                            else {
                                getAirConditionData(stationList, index + 1, gps);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AirConditionList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                }
            });
        }
        else {
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);
        }

    }



    @Override
    public void getGPSCoordinates() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Location Services Connected ");

        final boolean isPermissionEnabled = view.checkPermission();
        view.checkGpsEnabled();

        if(isPermissionEnabled)
        {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(15 * 1000)
                    .setFastestInterval(1 * 1000);

            if(lastLocation != null) {
                handleNewLocation(context , lastLocation);
            }
            else {
                try {
                    view.showSnackBarMessage("현재 위치를 찾을 수 없습니다.");
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.i(TAG, "GoogleApiClient is NOT Connected");
                }
            }
        }
        else {
            view.showSnackBarMessage(Const.STR_NEED_PERMISSION);
        }
    }

    private void handleNewLocation(final Context context, final Location location) {
        // x -> Longitude, y -> Latitude
        Log.i(TAG, location.toString());

        try {
            Geocoder gcd = new Geocoder(context, Locale.KOREA);
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            mRecent.getAddr().setAddr(makeAddressName(addresses));

            view.showSnackBarMessage(makeAddressName(addresses));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // 좌표변환
        convertCoordinates(location.getLatitude(), location.getLongitude());

    }

    private void convertCoordinates(final Double y, final Double x) {
        if (CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            String url = RetrofitClient.getGpsConvertUrl(String.valueOf(y) , String.valueOf(x));
            Log.v(TAG, "Check URL(convertCoordinates) : " + apiService.convertGpsData(url).request().url().toString());
            final Call<GpsData> requestForConvertingGpsData = apiService.convertGpsData(url);

            requestForConvertingGpsData.enqueue(new Callback<GpsData>() {
                @Override
                public void onResponse(Call<GpsData> call, Response<GpsData> response) {
                    if(response.isSuccessful())
                        getNearStationList(response.body().getTm_x(), response.body().getTm_y());
                }

                @Override
                public void onFailure(Call<GpsData> call, Throwable t) {
                    Log.v(TAG, Const.STR_FAIL_GET_DATA_FROM_SERVER);
                    view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                }
            });
        }
        else {
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);   // ?
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i(TAG, "Location services connection failed with code -> " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(context, location);
    }

    private void disconnectLocationService() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    private String makeAddressName(final List<Address> address) {
        mRecent.getAddr().setUmdName(address.get(0).getThoroughfare());
        String name = address.get(0).getAddressLine(0);
        return name.substring(5, name.length());
    }



    private void saveRecentData(RecentData recent) {
        Log.i(TAG, "save Recent Data to Preferences.");
        if(convertModeToInteger(recent.getCurrentMode()) != 0) {
            // Case : Loc_ONE, Loc_TWO, Loc_THREE
            Addresses addr = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[convertModeToInteger(recent.getCurrentMode())], Const.EMPTY_STRING, new Addresses());
            recent.setAddr(addr);
        }
        pref.putObject(SharedPreferences.RECENT_DATA[convertModeToInteger(recent.getCurrentMode())], recent);
    }


    private ArrayList<AirCondition> updateAirConditionDataFromNextStation(final AirCondition nextStationData, ArrayList<AirCondition> previousData) {
        Log.i(TAG, "  updateAirConditionDataFromNextStation , 누락된 정보 업데이트");

        if( previousData.get(0).getKhaiValue().equals("-") ) {
            previousData.get(0).setKhaiGrade(nextStationData.getKhaiGrade());
            previousData.get(0).setKhaiValue(nextStationData.getKhaiValue());
        }
        if( previousData.get(0).getPm10Value().equals("-") ) {
            previousData.get(0).setPm10Grade(nextStationData.getPm10Grade1h());
            previousData.get(0).setPm10Value(nextStationData.getPm10Value());
        }
        if( previousData.get(0).getPm25Value().equals("-") ) {
            previousData.get(0).setPm25Grade(nextStationData.getPm25Grade1h());
            previousData.get(0).setPm25Value(nextStationData.getPm25Value());
        }
        if( previousData.get(0).getO3Value().equals("-") ) {
            previousData.get(0).setO3Grade(nextStationData.getO3Grade());
            previousData.get(0).setO3Value(nextStationData.getO3Value());
        }

        // 나중에 Co, NO2, SO2 추가 가능

        return previousData;
    }

    private boolean isAllAirDataFilled(final AirCondition airData) {
        if ( airData.getKhaiValue().equals("-")
                || airData.getPm10Value().equals("-")
                || airData.getPm25Value().equals("-")
                || airData.getO3Value().equals("-") ) {
            return false;
        }
        return true;
    }

    @Override
    public int convertModeToInteger(final String mode) {
        if ( mode.equals(Const.MODE[1]) )
            return 1;
        if ( mode.equals(Const.MODE[2]) )
            return 2;
        if ( mode.equals(Const.MODE[3]) )
            return 3;
        return 0;
    }


    private boolean compareSavedDataTime(final RecentData recentData) {
        String currentTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00", Locale.getDefault());

        Calendar mCalendar = Calendar.getInstance();
        if (mCalendar.get(Calendar.HOUR_OF_DAY) <= 0) {
            mCalendar.add(Calendar.DATE , -1);
            currentTime = dateFormat.format(mCalendar.getTime()).replace("00:00" , "24:00");
        }
        else {
            currentTime = dateFormat.format(mCalendar.getTime());
        }

        try {
            Log.v(TAG, "현재시간 : " + currentTime + "\n최근저장시간 : " + recentData.getAirCondition().get(0).getDataTime() );
            if (currentTime.equals(recentData.getAirCondition().get(0).getDataTime()))
                return true;
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }

        return false;
    }

    private boolean updateWithRecentData(final String mode) {
        RecentData recentData = (RecentData) pref.getObject(SharedPreferences.RECENT_DATA[convertModeToInteger(mode)], Const.EMPTY_STRING, new RecentData());
        if (compareSavedDataTime(recentData)) {
            Log.i(TAG," updateWithRecentData()");
            view.updateDataToViews(recentData);
            return true;
        }
        return false;
    }



}
