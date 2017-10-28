package com.finedust.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finedust.model.Addresses;
import com.finedust.model.AirCondition;
import com.finedust.model.AirConditionList;
import com.finedust.model.Const;
import com.finedust.model.RecentData;
import com.finedust.model.Station;
import com.finedust.model.StationList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.SharedPreferences;
import com.finedust.widget.WidgetDark;
import com.finedust.widget.WidgetWhite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class WidgetService extends Service {
    private static final String TAG = WidgetService.class.getSimpleName();

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Context mContext;
    private SharedPreferences pref;

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    private RecentData mRecent;

    String widgetMode;
    String widgetTheme;
    Addresses data;

    public WidgetService() {
        this.mContext = this;
        this.pref = new SharedPreferences(this);
        mRecent = new RecentData();
        mRecent.setAddr(new Addresses());
        apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, " ** onStartCommand()");
        Context context = getmContext();

        try {
            Bundle mExtras = intent.getExtras();
            mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            widgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            widgetTheme = intent.getStringExtra(Const.WIDGET_THEME);

            //Temp Interval time
            String interval = pref.getValue(SharedPreferences.INTERVAL + mAppWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            int intervalNum = Integer.parseInt(interval) * 1000 * 60 * 60;

            Log.i(TAG, "  ** 알람설정(WidgetService)\n  mAppWidgetId : " + mAppWidgetId + " , THEME : " + widgetTheme +" , 인터벌 : " + interval + " 시간 , 모드 : " + widgetMode);

            // set Alarm
            long nextTime = System.currentTimeMillis() + intervalNum;
            Intent alarmRefresh = new Intent(context , WidgetService.class);
            alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id"), String.valueOf(mAppWidgetId)));
            alarmRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , mAppWidgetId);
            alarmRefresh.putExtra(Const.WIDGET_MODE, widgetMode);
            alarmRefresh.putExtra(Const.WIDGET_THEME, widgetTheme);

            if (mAppWidgetId != 0) {
                PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
                alarmManager.cancel(pendingAlarmRefresh);       // cancel the existing schedule before setting a new one.
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingAlarmRefresh);
            }

            // GET DATA FROM THE SERVER.
            checkCurrentMode(widgetMode);

        }
        catch (NullPointerException e) {
            Log.v(TAG, "AppWidgetId is not valid. [Wrong Widget Id].");
            //e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void cancelAlarmSchedule(Context context, int mAppWidgetId, String widgetTheme) {
        Log.i(TAG, "WidgetId [" + mAppWidgetId + "]'s schedule is deleted.");
        Intent alarmRefresh = new Intent(context , WidgetService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/") , String.valueOf(mAppWidgetId)));

        if (mAppWidgetId != 0) {
            PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarmManager.cancel(pendingAlarmRefresh);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        clearDisposable();
    }

    public void checkCurrentMode(String widgetMode) {
        Context context = getmContext();
        if (!CheckConnectivity.checkNetworkConnection(context))
            return;

        int mode = convertModeToInteger(widgetMode);
        if (mode != 0) {
            try {
                data = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[mode], Const.EMPTY_STRING, new Addresses());
                //Log.i(TAG, "x : " + data.getTmX() + " , y : " + data.getTmY());
                getAirConditionDataFromServer(data.getTmX(), data.getTmY());
            }
            catch (NullPointerException e) {
                // 사용자가 MEMORIZED LOCATIONS를 삭제했을 때. Fail to update widget.
                Intent failIntent = createFailureResponseIntent("삭제된 주소");
                context.sendBroadcast(failIntent);
            }
        }
    }

    private Intent createFailureResponseIntent(String failMessage) {
        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add("");
        gradeList.add("");
        gradeList.add("");

        ArrayList<String> valueList = new ArrayList<>();
        valueList.add("-");
        valueList.add("-");
        valueList.add("-");

        Intent response  = new Intent(Const.WIDGET_DARK_RESPONSE_FROM_SERVER);
        response.putExtra(Const.WIDGET_ID, String.valueOf(mAppWidgetId));
        response.putExtra(Const.WIDGET_MODE, Const.WIDGET_DELETED);
        response.putExtra(Const.WIDGET_LOCATION, failMessage);
        response.putStringArrayListExtra(Const.ARRAY_GRADE, gradeList);
        response.putStringArrayListExtra(Const.ARRAY_VALUE, valueList);

        return response;
    }


    public void getAirConditionDataFromServer(final String x, final String y) {
        Log.i(TAG, "[WidgetService] #getAirConditionDataFromServer( " + x + " , " + y + " )");

        // Run Progress Dial
        setProgressViewVisibility(widgetTheme, getmContext(), mAppWidgetId, true);

        Observable<StationList> stationListObservable = apiService.getNearStationList(x, y, Const.RETURNTYPE_JSON);
        addDisposable(
                stationListObservable
                .flatMap(new Function<StationList, Observable<AirConditionList>>() {
                    @Override
                    public Observable<AirConditionList> apply(@NonNull StationList stationList) throws Exception {
                        List<Station> list = stationList.getList();

                        if (!list.isEmpty()) {
                            mRecent.getAddr().setTmXTmY(x, y);
                            mRecent.setSavedStations(stationList.getList());

                            Log.i(TAG, "측정소명 : " + list.get(0).getStationName());
                            Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(list.get(0).getStationName());
                            return apiService.getAirConditionData(queryParams);
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AirConditionList>() {
                    @Override
                    public void accept(@NonNull AirConditionList airConditionList) throws Exception {
                        List<AirCondition> air = airConditionList.getList();
                        if (!air.isEmpty()) {
                            mRecent.setAirCondition(airConditionList.getList());
                            checkAllDataFilled(mRecent, 0);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, "[WidgetService] Fail to get data from server.");
                        setProgressViewVisibility(widgetTheme, getmContext(), mAppWidgetId, false);
                    }
                })
        );

    }

    private void getAirConditionDataAgain(final String stationName,  final int index) {
        Log.i(TAG, "재검색 측정소명 : " + stationName);

        Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);
        Observable<AirConditionList> airConditionListObservable = apiService.getAirConditionData(queryParams);
        addDisposable(airConditionListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AirConditionList>() {
                    @Override
                    public void accept(@NonNull AirConditionList airConditionList) throws Exception {
                        List<AirCondition> nextStationData = airConditionList.getList();

                        if (!nextStationData.isEmpty()) {
                            ArrayList<AirCondition> update = updateAirConditionDataFromNextStation(nextStationData.get(0), mRecent.getAirCondition());
                            mRecent.setAirCondition( update );
                            checkAllDataFilled(mRecent, index);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, "[WidgetService] Fail to get data from server.");
                        setProgressViewVisibility(widgetTheme, getmContext(), mAppWidgetId, false);
                    }
                })
        );
    }

    private void checkAllDataFilled(RecentData recentData, int index) {
        if (isAllAirDataFilled(recentData.getAirCondition().get(0)) || (index >= recentData.getSavedStations().size() - 1)) {
            updateDataWithIntent(recentData);
        }
        else {
            // research with next station.
            getAirConditionDataAgain(recentData.getSavedStations().get(index + 1).getStationName(), index + 1);
        }
    }

    private void updateDataWithIntent(RecentData recent) {
        recent.setAddr(data);

        if (pref.getValue(SharedPreferences.GRADE_MODE, Const.ON_OFF[1]).equals(Const.ON_OFF[0])) {
            recent = judgeSelfGrade(recent);
        }
        pref.putObject(SharedPreferences.RECENT_DATA[convertModeToInteger(widgetMode)], recent);

        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add(recent.getAirCondition().get(0).getPm10Grade1h());
        gradeList.add(recent.getAirCondition().get(0).getPm25Grade1h());
        gradeList.add(recent.getAirCondition().get(0).getKhaiGrade());

        ArrayList<String> valueList = new ArrayList<>();
        valueList.add(recent.getAirCondition().get(0).getPm10Value());
        valueList.add(recent.getAirCondition().get(0).getPm25Value());
        valueList.add(recent.getAirCondition().get(0).getKhaiValue());


        Intent response = getResponseForWidgetTheme(widgetTheme);
        response.putExtra(Const.WIDGET_ID, String.valueOf(mAppWidgetId));
        response.putExtra(Const.WIDGET_MODE, widgetMode);
        response.putExtra(Const.WIDGET_LOCATION, data.getAddr());
        response.putStringArrayListExtra(Const.ARRAY_GRADE, gradeList);
        response.putStringArrayListExtra(Const.ARRAY_VALUE, valueList);

        setProgressViewVisibility(widgetTheme, getmContext(), mAppWidgetId, false);

        mContext.sendBroadcast(response);
    }

    private Intent getResponseForWidgetTheme(String widgetTheme) {
        if (widgetTheme.equals(Const.DARKWIDGET)) {
            return new Intent(Const.WIDGET_DARK_RESPONSE_FROM_SERVER);
        }
        else {
            return new Intent(Const.WIDGET_WHITE_RESPONSE_FROM_SERVER);
        }
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

    public int convertModeToInteger(final String mode) {
        if ( mode.equals(Const.MODE[1]) )
            return 1;
        if ( mode.equals(Const.MODE[2]) )
            return 2;
        if ( mode.equals(Const.MODE[3]) )
            return 3;
        return 0;
    }

    private void setProgressViewVisibility(String widgetTheme, Context context, int mAppWidgetId, boolean on) {
        if (widgetTheme.equals(Const.DARKWIDGET))
            WidgetDark.setProgressViewVisibility(context, mAppWidgetId, on);
        else
            WidgetWhite.setProgressViewVisibility(context, mAppWidgetId, on);
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void clearDisposable() {
        compositeDisposable.clear();
    }


}
