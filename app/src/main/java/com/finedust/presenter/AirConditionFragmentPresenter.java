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
import com.finedust.model.Station;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


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

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    public AirConditionFragmentPresenter(Views.AirConditionFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        mRecent = new RecentData();
        mRecent.setAddr(new Addresses());
        pref = new SharedPreferences(context);
        apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();
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
            try {
                if ( num == 1 ) {
                    Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                    getAirConditionData(data.getTmX(), data.getTmY());
                }
                else if ( num == 2 ) {
                    Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                    getAirConditionData(data.getTmX(), data.getTmY());
                }
                else if ( num == 3 ) {
                    Addresses data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[num], Const.EMPTY_STRING, new Addresses());
                    getAirConditionData(data.getTmX(), data.getTmY());
                }
                else  {
                    Log.v(TAG,"   GPS 이용해서 좌표구하기 실행" );
                    getGPSCoordinates();
                }
            }
            catch (NullPointerException e) {
                // 저장위치를 삭제 했으나, 기존의 남아있던 위젯으로 앱을 작동했을 때 MEMORIZED_LOCATIONS가 Null 일 수 있다.
                pref.put(SharedPreferences.CURRENT_MODE, Const.MODE[0]);
                mRecent.setCurrentMode(Const.MODE[0]);
                getGPSCoordinates();
            }
        }
    }

    /**
     * 좌표 -> 측정소명 -> 대기정보 요청하는 메소드
     * */

    @Override
    public void getAirConditionData(final String x, final String y) {
        Log.i(TAG, "#getAirConditionData( " + x + " , " + y + " )");
        // Checking InternetConnection
        if (!CheckConnectivity.checkNetworkConnection(context))
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);

        else {
            Observable<StationList> stationListObservable = apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON);
            addDisposable(
                stationListObservable
                .flatMap(new Function<StationList, Observable<AirConditionList>>() {
                     @Override
                     public Observable<AirConditionList> apply(@io.reactivex.annotations.NonNull StationList stationList) throws Exception {
                         List<Station> list = stationList.getList();

                         if(list.isEmpty())
                             view.showSnackBarMessage("주변의 측정소를 찾지 못하였습니다.");
                         else {
                             mRecent.getAddr().setTmXTmY(x, y);
                             mRecent.setSavedStations(stationList.getList());

                             Log.i(TAG, "#측정소명 : " + list.get(0).getStationName());

                             Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(list.get(0).getStationName());

                             return apiService.getAirConditionData(queryParams);
                         }
                         return null;
                     }})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AirConditionList>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AirConditionList airConditionList) throws Exception {
                        List<AirCondition> air = airConditionList.getList();
                        if(air.isEmpty())
                            view.showSnackBarMessage("대기오염 정보를 찾지 못하였습니다.");
                        else {
                            mRecent.setAirCondition(airConditionList.getList());
                            checkAllDataFilled(mRecent, 0);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, "Fail to get data from server[getAirConditionData()]");
                        view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                    }
                })
            );
        }
    }

    private void getAirConditionDataAgain(final String stationName , final int index) {
        Log.i(TAG, "#재검색 측정소명 : " + stationName);

        Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);
        Observable<AirConditionList> airConditionListObservable = apiService.getAirConditionData(queryParams);
        addDisposable( airConditionListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AirConditionList>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AirConditionList airConditionList) throws Exception {
                        List<AirCondition> nextStationData = airConditionList.getList();

                        if(nextStationData.isEmpty())
                            view.showSnackBarMessage("대기오염 정보를 찾지 못하였습니다.");
                        else {
                            ArrayList<AirCondition> update = updateAirConditionDataFromNextStation(nextStationData.get(0), mRecent.getAirCondition());
                            mRecent.setAirCondition( update );
                            checkAllDataFilled(mRecent, index);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, "Fail to get data from server");
                        view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                    }
                })
        );
    }


    /**
     * 누락된 대기정보를 확인하고 누락된 정보를 재탐색하도록 요청하는 메소드.
     * */

    private void checkAllDataFilled(RecentData recentData, int index) {
        if ( isAllAirDataFilled(recentData.getAirCondition().get(0)) || (index >= recentData.getSavedStations().size() - 1) ) {
            Log.i(TAG, " #### SAVE RECENTDATA");
            saveRecentData(recentData);
            view.updateDataToViews(recentData);
        }
        else {
            getAirConditionDataAgain(recentData.getSavedStations().get(index + 1).getStationName(), index + 1);
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
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
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
        Log.i(TAG, "#convertCoordinates( " + y + " , " + x + " )");
        if (!CheckConnectivity.checkNetworkConnection(context))
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);

        else {
            Observable<GpsData> gpsDataObservable = apiService.convertGpsData(RetrofitClient.getGpsConvertUrl(String.valueOf(y) , String.valueOf(x)));
            addDisposable(
                gpsDataObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GpsData>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull GpsData gpsData) throws Exception {
                            if (gpsData != null) {
                                getAirConditionData(gpsData.getTm_x(), gpsData.getTm_y());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Log.i(TAG, "Fail to convert WGS84 Coordinates to TM Coordinates.");
                        }
                    })
            );
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
        if (convertModeToInteger(recent.getCurrentMode()) != 0) {
            // Case : Loc_ONE, Loc_TWO, Loc_THREE
            Addresses addr = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[convertModeToInteger(recent.getCurrentMode())], Const.EMPTY_STRING, new Addresses());
            recent.setAddr(addr);
        }
        if (pref.getValue(SharedPreferences.GRADE_MODE, Const.ON_OFF[1]).equals(Const.ON_OFF[0])) {
            Log.i(TAG, "SelfGrade : ON");
            recent = judgeSelfGrade(recent);
        }
        pref.putObject(SharedPreferences.RECENT_DATA[convertModeToInteger(recent.getCurrentMode())], recent);
    }

    private RecentData judgeSelfGrade(RecentData recent) {
        if (!recent.getAirCondition().get(0).getPm10Grade1h().equals("-")) {
            int value = Integer.parseInt(recent.getAirCondition().get(0).getPm10Value());

            int[] pm10_grade = {
                    Integer.parseInt(pref.getValue(Const.SELF_GRADE_PM10[0],"0")),
                    Integer.parseInt(pref.getValue(Const.SELF_GRADE_PM10[1],"0")),
                    Integer.parseInt(pref.getValue(Const.SELF_GRADE_PM10[2],"0"))
            };

            if (value <= pm10_grade[0])
                recent.getAirCondition().get(0).setPm10Grade1h(Const.GRADE_BEST);
            else if (value <= pm10_grade[1])
                recent.getAirCondition().get(0).setPm10Grade1h(Const.GRADE_GOOD);
            else if (value <= pm10_grade[2])
                recent.getAirCondition().get(0).setPm10Grade1h(Const.GRADE_BAD);
            else
                recent.getAirCondition().get(0).setPm10Grade1h(Const.GRADE_VERYBAD);
        }

        if (!recent.getAirCondition().get(0).getPm25Grade1h().equals("-")) {
            int value = Integer.parseInt(recent.getAirCondition().get(0).getPm25Value());

            int[] pm25_grade = {
                    Integer.parseInt( pref.getValue(Const.SELF_GRADE_PM25[0], "0") ),
                    Integer.parseInt( pref.getValue(Const.SELF_GRADE_PM25[1], "0") ),
                    Integer.parseInt( pref.getValue(Const.SELF_GRADE_PM25[2], "0") )
            };

            if (value <= pm25_grade[0])
                recent.getAirCondition().get(0).setPm25Grade1h(Const.GRADE_BEST);
            else if (value <= pm25_grade[1])
                recent.getAirCondition().get(0).setPm25Grade1h(Const.GRADE_GOOD);
            else if (value <= pm25_grade[2])
                recent.getAirCondition().get(0).setPm25Grade1h(Const.GRADE_BAD);
            else
                recent.getAirCondition().get(0).setPm25Grade1h(Const.GRADE_VERYBAD);
        }

        return recent;
    }

    private ArrayList<AirCondition> updateAirConditionDataFromNextStation(final AirCondition nextStationData, ArrayList<AirCondition> previousData) {

        if( previousData.get(0).getKhaiValue().equals("-") ) {
            previousData.get(0).setKhaiGrade(nextStationData.getKhaiGrade());
            previousData.get(0).setKhaiValue(nextStationData.getKhaiValue());
        }
        if( previousData.get(0).getPm10Value().equals("-") ) {
            previousData.get(0).setPm10Grade1h(nextStationData.getPm10Grade1h());
            previousData.get(0).setPm10Value(nextStationData.getPm10Value());
        }
        if( previousData.get(0).getPm25Value().equals("-") ) {
            previousData.get(0).setPm25Grade1h(nextStationData.getPm25Grade1h());
            previousData.get(0).setPm25Value(nextStationData.getPm25Value());
        }
        if( previousData.get(0).getO3Value().equals("-") ) {
            previousData.get(0).setO3Grade(nextStationData.getO3Grade());
            previousData.get(0).setO3Value(nextStationData.getO3Value());
        }
        if( previousData.get(0).getNo2Value().equals("-") ) {
            previousData.get(0).setNo2Grade(nextStationData.getNo2Grade());
            previousData.get(0).setNo2Value(nextStationData.getNo2Value());
        }
        if( previousData.get(0).getCoValue().equals("-") ) {
            previousData.get(0).setCoGrade(nextStationData.getCoGrade());
            previousData.get(0).setCoValue(nextStationData.getCoValue());
        }
        if( previousData.get(0).getSo2Value().equals("-") ) {
            previousData.get(0).setSo2Grade(nextStationData.getSo2Grade());
            previousData.get(0).setSo2Value(nextStationData.getSo2Value());
        }


        return previousData;
    }


    private boolean isAllAirDataFilled(final AirCondition airData) {
        if ( airData.getKhaiValue().equals("-")
                || airData.getPm10Value().equals("-")
                || airData.getPm25Value().equals("-")
                || airData.getO3Value().equals("-")
                || airData.getNo2Value().equals("-")
                || airData.getCoValue().equals("-")
                || airData.getSo2Value().equals("-") ) {
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

    @Override
    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }


    @Override
    public void clearDisposable() {
        compositeDisposable.clear();
    }


}
