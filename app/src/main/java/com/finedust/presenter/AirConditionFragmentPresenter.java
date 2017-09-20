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

import com.finedust.model.AirConditionList;
import com.finedust.model.Const;
import com.finedust.model.GpsData;
import com.finedust.model.Addresses;
import com.finedust.model.RecentData;
import com.finedust.model.StationList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.Views;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            RecentData data = (RecentData) pref.getObject(Const.RECENT_DATA, Const.EMPTY_STRING, new RecentData());
            Log.v(TAG , "[ RCENT_DATA from SharedPreferences ]"
                    + "\n현재모드: " +  data.getCurrentMode()
                    + "\n저장된 주소 : " + data.getAddr().getAddr()
                    + "\n동이름 : " + data.getAddr().getUmdName() + "  , 좌표 : " + data.getAddr().getTmX() + " ," + data.getAddr().getTmY()
                    + "\n측정소[1] : " + data.getSavedStations().get(0).getAddr() + " , " + data.getSavedStations().get(0).getStationName() + " , " + data.getSavedStations().get(0).getTm()
                    + "\n측정소[2] : " + data.getSavedStations().get(1).getAddr() + " , " + data.getSavedStations().get(1).getStationName() + " , " + data.getSavedStations().get(1).getTm()
                    + "\n측정소[3] : " + data.getSavedStations().get(2).getAddr() + " , " + data.getSavedStations().get(2).getStationName() + " , " + data.getSavedStations().get(2).getTm()
                    + "\n시간 : " + data.getAirCondition().getDataTime()
                    + "\nKhai : " + data.getAirCondition().getKhaiValue()
                    + "  //  PM10 : " + data.getAirCondition().getPm10Value()
                    + "  //  PM2.5 : " + data.getAirCondition().getPm25Value()
                    + "  //  vNO2 : " + data.getAirCondition().getNo2Value()
                    + "  //  CO : " + data.getAirCondition().getCoValue()
                    + "  //  O3 : " + data.getAirCondition().getO3Value()
                    + "  //  SO2 : " + data.getAirCondition().getSo2Value()
            );
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private int convertModeToInteger(String mode) {
        if (mode.equals(Const.MODE[1]))
            return 1;
        if (mode.equals(Const.MODE[2]))
            return 2;
        if (mode.equals(Const.MODE[3]))
            return 3;
        return 0;
    }

    @Override
    public void checkCurrentMode(String mode) {
        mRecent.setCurrentMode(mode);
        int num = convertModeToInteger(mode);

        if (num == 1) {
            Addresses data = (Addresses) pref.getObject(Const.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
            getNearStationList(data.getTmX(), data.getTmY());
        }
        else if (num == 2) {
            Addresses data = (Addresses) pref.getObject(Const.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
            getNearStationList(data.getTmX(), data.getTmY());
        }
        else if (num == 3) {
            Addresses data = (Addresses) pref.getObject(Const.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
            getNearStationList(data.getTmX(), data.getTmY());
        }
        else  {
            Log.i(TAG,"   GPS 이용해서 좌표구하기 실행" );
            getGPSCoordinates();
        }
    }


    public boolean compareSavedDataTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00");
        String currentTime = dateFormat.format(Calendar.getInstance().getTime());

        try {
            RecentData recentData = (RecentData) pref.getObject(Const.RECENT_DATA, Const.EMPTY_STRING, new RecentData());
            Log.i(TAG, "compareDatatime -> 최근 저장 시간 : " + recentData.getAirCondition().getDataTime()
            + "\n현재시간 : " + currentTime);

            if (currentTime.equals(recentData.getAirCondition().getDataTime())) {
                Log.i(TAG, "현재시간과 최근 저장된 데이터 시간이 동일 -> return true");
                return true;
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updateWithRecentData(String mode) {
        if (compareSavedDataTime()) {
            RecentData recentData = (RecentData) pref.getObject(Const.RECENT_DATA, Const.EMPTY_STRING, new RecentData());
        }
        else {
            // 그냥 리퀘스트해서 데이터 받아와야함.
        }

    }



    @Override
    public void getNearStationList(String x, String y) {
        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            final Call<StationList> requestForNearStationList = apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON);
            Log.v(TAG, "Check URL(Near Station Lists : " + apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON).request().url().toString());

            requestForNearStationList.enqueue(new Callback<StationList>() {
                @Override
                public void onResponse(Call<StationList> call, Response<StationList> response) {
                    if(response.isSuccessful()) {
                        if (response.body().getTotalCount().equals(0)) {
                            view.showSnackBarMessage("주변의 측정소를 찾지 못하였습니다.");
                        }
                        else {
                            mRecent.setSavedStations(response.body().getList());

                            getAirConditionData(response.body().getList().get(0).getStationName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StationList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage("Fail to get data from server(getNearStationiList)");
                }
            });
        }
        else {
            view.showSnackBarMessage("Internet Connection is not available now.");
        }
    }


    /**
     * 측정소명을 파라미터로 해당 측정소의 대기정보를 요청하는 메소드.
     * */

    @Override
    public void getAirConditionData(String stationName) {
        view.showToastMessage("[ " + stationName + " ] 측정소의 정보를 검색합니다.");

        // Checking InternetConnection
        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);

            Log.v(TAG, "Check URL : " + apiService.getAirConditionData(queryParams).request().url().toString());
            final Call<AirConditionList> requestForAirConditionData = apiService.getAirConditionData(queryParams);

            requestForAirConditionData.enqueue(new Callback<AirConditionList>() {
                @Override
                public void onResponse(Call<AirConditionList> call, Response<AirConditionList> response) {
                    if(response.isSuccessful()) {
                        if (response.body().getTotalCount().equals(0)) {
                            view.showSnackBarMessage("대기오염 정보를 찾지 못하였습니다.");
                        }
                        else {
                            mRecent.setAirCondition(response.body().getList().get(0));

                            Log.i(TAG, "ORDER to view : updateAirConditionData");
                            view.updateAirConditionData(response.body().getList());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AirConditionList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage("Fail to get data from server");
                }
            });
        }
        else {
            view.showSnackBarMessage("Internet Connection is not available now.");
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
        Log.i(TAG, "Location Services Connected ");

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
                    Log.i(TAG, "GoogleApiClient is NOT Connected yet");
                }
            }
        }
        else {
            view.showSnackBarMessage("권한 허가가 필요합니다.");
        }
    }

    private void handleNewLocation(Context context, Location location) {
        // x -> Longitude, y -> Latitude
        Log.i(TAG, location.toString());
        {
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
    }

    private void convertCoordinates(Double y, Double x) {
        if (CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            String url = RetrofitClient.getGpsConvertUrl(String.valueOf(y) , String.valueOf(x));
            Log.v(TAG, "Check URL : " + apiService.convertGpsData(url).request().url().toString());
            final Call<GpsData> requestForConvertingGpsData = apiService.convertGpsData(url);

            requestForConvertingGpsData.enqueue(new Callback<GpsData>() {
                @Override
                public void onResponse(Call<GpsData> call, Response<GpsData> response) {
                    if(response.isSuccessful()) {
                        mRecent.getAddr().setTmXTmY(response.body().getTm_x(), response.body().getTm_y());
                        // 얻은 좌표값을 사용해서 근접 측정소 목록 정보 요청하기.
                        getNearStationList(response.body().getTm_x(), response.body().getTm_y());
                    }
                }

                @Override
                public void onFailure(Call<GpsData> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage("Fail to get data from server(convertCoordinates)");
                }
            });


        }
        else {
            view.showSnackBarMessage("Internet Connection is not available now.");
        }
    }

    private String makeAddressName(List<Address> addr) {
        mRecent.getAddr().setUmdName(addr.get(0).getThoroughfare());
        if(addr.get(0).getAdminArea() == null)
            return addr.get(0).getLocality() + " " + addr.get(0).getSubLocality() + " " + addr.get(0).getThoroughfare();
        return addr.get(0).getAdminArea() + " " + addr.get(0).getLocality() + " " + addr.get(0).getThoroughfare();
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

    @Override
    public void saveRecentData() {
        if(convertModeToInteger(mRecent.getCurrentMode()) != 0) {
            Addresses addr = (Addresses) pref.getObject(Const.MEMORIZED_LOCATIONS[convertModeToInteger(mRecent.getCurrentMode())], Const.EMPTY_STRING, new Addresses());
            mRecent.setAddr(addr);
        }
        pref.putObject(Const.RECENT_DATA, mRecent);
    }



}
