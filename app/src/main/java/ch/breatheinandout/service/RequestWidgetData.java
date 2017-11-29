package ch.breatheinandout.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import ch.breatheinandout.model.Addresses;
import ch.breatheinandout.model.AirCondition;
import ch.breatheinandout.model.AirConditionList;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.model.RecentData;
import ch.breatheinandout.model.Station;
import ch.breatheinandout.model.StationList;
import ch.breatheinandout.retrofit.api.ApiService;
import ch.breatheinandout.retrofit.api.RetrofitClient;
import ch.breatheinandout.utils.CheckConnectivity;
import ch.breatheinandout.utils.AppSharedPreferences;
import ch.breatheinandout.widget.WidgetDark;
import ch.breatheinandout.widget.WidgetWhite;

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

public class RequestWidgetData {
    private static final String TAG = RequestWidgetData.class.getSimpleName();

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Context context;
    private AppSharedPreferences pref;
    private ApiService apiService;

    private CompositeDisposable compositeDisposable;
    private RecentData mRecent;

    private String widgetMode;
    private String widgetTheme;
    private Addresses data;

    private String ACTION_RESPONSE_WIDGET;

    public RequestWidgetData() {
        compositeDisposable = new CompositeDisposable();
    }

    public RequestWidgetData(Context context, int mAppWidgetId, String widgetMode, String widgetTheme) {
        this.mAppWidgetId = mAppWidgetId;
        this.context = context;
        this.widgetMode = widgetMode;
        this.widgetTheme = widgetTheme;

        this.pref = new AppSharedPreferences(context);
        this.apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();

        this.mRecent = new RecentData();
        mRecent.setAddr(new Addresses());

        if (widgetTheme.equals(Const.DARKWIDGET)) {
            ACTION_RESPONSE_WIDGET = Const.WIDGET_DARK_RESPONSE_FROM_SERVER;
        }
        else {
            ACTION_RESPONSE_WIDGET = Const.WIDGET_WHITE_RESPONSE_FROM_SERVER;
        }
    }

    public void startGetDataFromServer(String widgetMode) {
        if (!CheckConnectivity.checkNetworkConnection(context))
            return;

        mRecent.setCurrentMode(widgetMode);
        int mode = convertModeToInteger(widgetMode);
        if (mode != 0) {
            try {
                data = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[mode], Const.EMPTY_STRING, new Addresses());
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

    private void getAirConditionDataFromServer(final String x, final String y) {
        Log.i(TAG, "[WidgetService] #getAirConditionDataFromServer( " + x + " , " + y + " )");

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

                                    Log.i(TAG, "[WidgetService] 측정소명 : " + list.get(0).getStationName());
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
                            public void accept(@io.reactivex.annotations.NonNull AirConditionList airConditionList) throws Exception {
                                List<AirCondition> air = airConditionList.getList();
                                if (air.isEmpty()) {
                                    AirCondition dummy = new AirCondition();
                                    air.add(dummy);
                                }
                                mRecent.setAirCondition(air);
                                checkAllDataFilled(mRecent, 0);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.v(TAG, "[WidgetService] Fail to get data from server.");
                                setProgressVisibility(context, mAppWidgetId, false);
                            }
                        })
        );

    }

    private void getAirConditionDataAgain(final String stationName,  final int index) {
        Log.i(TAG, "[WidgetService] 재검색 측정소명 : " + stationName);

        Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);
        Observable<AirConditionList> airConditionListObservable = apiService.getAirConditionData(queryParams);
        addDisposable(airConditionListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AirConditionList>() {
                    @Override
                    public void accept(@NonNull AirConditionList airConditionList) throws Exception {
                        List<AirCondition> nextStationData = airConditionList.getList();

                        if (nextStationData.isEmpty()) {
                            AirCondition dummy = new AirCondition();
                            nextStationData.add(dummy);
                        }
                        List<AirCondition> update = updateAirConditionDataFromNextStation(nextStationData.get(0), mRecent.getAirCondition());
                        mRecent.setAirCondition( update );
                        checkAllDataFilled(mRecent, index);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, "[WidgetServiceAgain] Fail to get data from server.");
                        throwable.printStackTrace();
                        setProgressVisibility(context, mAppWidgetId, false);
                    }
                })
        );
    }

    private void checkAllDataFilled(RecentData recentData, int index) {
        if (isAllAirDataFilled(recentData.getAirCondition().get(0)) || (index >= recentData.getSavedStations().size() - 1)) {
            getResultIntent(recentData);
        }
        else {
            // research with next station.
            getAirConditionDataAgain(recentData.getSavedStations().get(index + 1).getStationName(), index + 1);
        }
    }

    private void getResultIntent(RecentData recent) {
        recent.setAddr(data);

        if (pref.getValue(AppSharedPreferences.GRADE_MODE, Const.ON_OFF[1]).equals(Const.ON_OFF[0])) {
            recent = judgeSelfGrade(recent);
        }
        pref.putObject(AppSharedPreferences.RECENT_DATA[convertModeToInteger(widgetMode)], recent);

        AirCondition air = recent.getAirCondition().get(0);
        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add(air.getPm10Grade1h());
        gradeList.add(air.getPm25Grade1h());
        gradeList.add(air.getKhaiGrade());

        ArrayList<String> valueList = new ArrayList<>();
        valueList.add(air.getPm10Value());
        valueList.add(air.getPm25Value());
        valueList.add(air.getKhaiValue());


        Intent response = new Intent(ACTION_RESPONSE_WIDGET);
        response.putExtra(Const.WIDGET_ID, String.valueOf(mAppWidgetId));
        response.putExtra(Const.WIDGET_MODE, widgetMode);
        response.putExtra(Const.WIDGET_LOCATION, data.getAddr());
        response.putStringArrayListExtra(Const.ARRAY_GRADE, gradeList);
        response.putStringArrayListExtra(Const.ARRAY_VALUE, valueList);

        context.sendBroadcast(response);
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

    private List<AirCondition> updateAirConditionDataFromNextStation(final AirCondition nextStationData, List<AirCondition> previousData) {

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


    private Intent createFailureResponseIntent(String failMessage) {
        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add("");
        gradeList.add("");
        gradeList.add("");

        ArrayList<String> valueList = new ArrayList<>();
        valueList.add("-");
        valueList.add("-");
        valueList.add("-");

        Intent response  = new Intent(ACTION_RESPONSE_WIDGET);
        response.putExtra(Const.WIDGET_ID, String.valueOf(mAppWidgetId));
        response.putExtra(Const.WIDGET_MODE, Const.WIDGET_DELETED);
        response.putExtra(Const.WIDGET_LOCATION, failMessage);
        response.putStringArrayListExtra(Const.ARRAY_GRADE, gradeList);
        response.putStringArrayListExtra(Const.ARRAY_VALUE, valueList);

        return response;
    }


    public static void cancelAlarmSchedule(Context context, int mAppWidgetId, String widgetTheme) {
        Log.i(TAG, "WidgetId [" + mAppWidgetId + "]'s schedule is deleted.");
        Intent alarmRefresh = new Intent(context , WidgetDarkService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/") , String.valueOf(mAppWidgetId)));

        if (mAppWidgetId != 0) {
            PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarmManager.cancel(pendingAlarmRefresh);
        }
    }

    private int convertModeToInteger(final String mode) {
        if ( mode.equals(Const.MODE[1]) )
            return 1;
        if ( mode.equals(Const.MODE[2]) )
            return 2;
        if ( mode.equals(Const.MODE[3]) )
            return 3;
        return 0;
    }

    private void setProgressVisibility(Context context, int id, boolean on)  {
        if (widgetTheme.equals(Const.DARKWIDGET))
            WidgetDark.setProgressViewVisibility(context, id, on);
        else if (widgetTheme.equals(Const.WHITEWIDGET))
            WidgetWhite.setProgressViewVisibility(context, id, on);
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void clearDisposable() {
        compositeDisposable.clear();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.i(TAG, "[Clear Disposable]");
        clearDisposable();
    }
}
