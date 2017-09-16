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

import com.finedust.model.AirCondition;
import com.finedust.model.AirConditionList;
import com.finedust.model.Const;
import com.finedust.model.GpsData;
import com.finedust.model.RecentData;
import com.finedust.model.Station;
import com.finedust.model.StationList;
import com.finedust.model.pref.MemorizedAddress;
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
import java.util.ArrayList;
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

    SharedPreferences pref;

    private GoogleApiClient googleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public AirConditionFragmentPresenter(Views.AirConditionFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        pref = new SharedPreferences(context);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()\ndisconnectLocationService");
        disconnectLocationService();
        try {
            StationList list = (StationList) pref.getObject(Const.RECENT_STATION_LIST, Const.EMPTY_STRING, new StationList());
            Log.i(TAG + " pref에 저장된 측정소 정보", list.getList().get(0).getAddr()
                    + "\n" + list.getList().get(1).getAddr()
                    + "\n" + list.getList().get(2).getAddr());

            RecentData data = (RecentData) pref.getObject(Const.RECENT_DATA, Const.EMPTY_STRING, new RecentData());
            Log.i(TAG , "pref에 저장된 최근 정보"
                    + "\nMode : " +  data.getMode()
                    + "\n저장된 주소 : " + data.getAddr().getMemorizedAddress()
                    + "\nPM10 : " + data.getAirCondition().getPm10Value() + "\nPM2.5 : " + data.getAirCondition().getPm25Value()
            );
        }
        catch (NullPointerException e) {
            e.printStackTrace();
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
                            ArrayList<Station> stationList = response.body().getList();
                            for(Station stn : stationList) {
                                Log.i(TAG, "   측정소명 : " + stn.getStationName()+ "\n   주소 : " + stn.getAddr() + "\n   거리 : " + stn.getTm());

                            }
                            pref.putObject(Const.RECENT_STATION_LIST, response.body());

                            getAirConditionData(context, stationList.get(0).getStationName());
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
    public void getAirConditionData(Context context, String stationName) {
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
                            ArrayList<AirCondition> airConditionList = response.body().getList();
                            Log.i(TAG, "ORDER to view : updateAirConditionData");

                            String MODE = pref.getValue(Const.CURRENT_MODE, Const.MODE[0]);
                            StationList list = (StationList) pref.getObject(Const.RECENT_STATION_LIST, Const.EMPTY_STRING, new StationList());
                            MemorizedAddress addr = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[0], Const.EMPTY_STRING, new MemorizedAddress()); //*
                            AirCondition air = airConditionList.get(0);
                            RecentData recentData = new RecentData(MODE, addr, list.getList(), air);
                            pref.putObject(Const.RECENT_DATA, recentData);

                            view.updateAirConditionData(airConditionList);
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



}
