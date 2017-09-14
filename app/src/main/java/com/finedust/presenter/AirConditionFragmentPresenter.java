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
import com.finedust.model.GpsData;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
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
    Context context;

    GoogleApiClient googleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    boolean isPermissionEnabled = false;

    public AirConditionFragmentPresenter(Views.AirConditionFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()\ndisconnectLocationService");
        disconnectLocationService();
    }

    /**
     * 측정소명을 파라미터로 해당 측정소의 대기정보를 요청하는 메소드.
     * */

    @Override
    public void getAirConditionData(Context context, String stationName) {

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
                        ArrayList<AirCondition> airConditionList = response.body().getList();
                        if (airConditionList.size() > 0) {
                            Log.i(TAG, "ORDER to view : updateAirConditionData");
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
    public void onSampleButtonClicked() {
        // ... Business Logic works
        Log.i(TAG, "Working on some Business Logic when the button is clicked.");

        // update UI with the results.
        String msg = "수행결과값 : 버튼이 클릭 되었습니다.";

        view.showToastMessage(msg);
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

        isPermissionEnabled = view.checkPermission();
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
        Log.i(TAG, location.toString());
        {
            Geocoder gcd = new Geocoder(context, Locale.KOREA);

            GpsData CoordData = new GpsData();
            CoordData.setWgs_x(String.valueOf(location.getLongitude()));
            CoordData.setWgs_y(String.valueOf(location.getLatitude()));

            Log.i(TAG, " # Check Wgs Coord : " + CoordData.getWgs_x() + " , " + CoordData.getWgs_y());

            try {
                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                view.showSnackBarMessage(makeAddressName(addresses));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // 좌표변환
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
