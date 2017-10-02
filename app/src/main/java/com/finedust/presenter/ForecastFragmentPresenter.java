package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.Const;
import com.finedust.model.Forecast;
import com.finedust.model.ForecastList;
import com.finedust.model.RecentForecast;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.Views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ForecastFragmentPresenter implements Presenter.ForecastFragmentPresenter {
    private static final String TAG = ForecastFragmentPresenter.class.getSimpleName();
    private Views.ForecastFragmentView view;
    private Context context;

    private SharedPreferences pref;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    public ForecastFragmentPresenter(Views.ForecastFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        pref = new SharedPreferences(context);
        apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getForecastData() {
        String date =  calculateCurrentDate();

        if (date != null) {
            Log.i(TAG, "date : " + date);
            getForecastDataFromServer(date);
        }
    }

    private String calculateCurrentDate() {
        String date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar mCalendar = Calendar.getInstance();

        int currentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        if (currentHour >= 0 && currentHour < 5)
            mCalendar.add(Calendar.DATE, -1);

        //SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String currentDay = dateFormat.format(mCalendar.getTime()).substring(8,10);

        if (!compareSavedDataTime(currentDay, String.valueOf(currentHour))) {
            date = dateFormat.format(mCalendar.getTime());
            Log.i(TAG, "current Hour : " + currentHour + " , Day : " + currentDay + "\nDate : " + date);
            view.showToastMessage("current Hour : " + currentHour + " , Day : " + currentDay + "\nDate : " + date);

            return date;
        }

        return null;
    }


    private boolean compareSavedDataTime(String currentDay , String currentHour) {
        try {
            RecentForecast recentForecast = (RecentForecast) pref.getObject(SharedPreferences.RECENT_DATA_FORECAST, Const.EMPTY_STRING, new RecentForecast());

            Log.i(TAG, "currentDay : " + currentDay + " , pastDay : " + recentForecast.getSaveDay());
            if (String.valueOf(currentDay).equals(recentForecast.getSaveDay())) {
                //최근정보로 업데이트
                int pastHour = Integer.parseInt(recentForecast.getPM10().getDataTime().substring(11,13));
                String msg = "pastHour : " + pastHour
                        +"\n과거시간 페이즈 : " + getTimePhaseZone(pastHour)
                        +"\n현재시간 페이즈 : " + getTimePhaseZone(Integer.parseInt(currentHour))
                        +"\n비교결과 : " + (getTimePhaseZone(pastHour) == getTimePhaseZone(Integer.parseInt(currentHour)));
                Log.i(TAG, msg);
                view.showToastMessage(msg);

                //최근정보로 업데이트
                if ( getTimePhaseZone(pastHour) == getTimePhaseZone(Integer.parseInt(currentHour)) ) {
                    view.saveDataToPreferences(recentForecast);

                    return true;
                }
            }

            return false;

        }
        catch (NullPointerException e) {
            return false;
        }
    }


    @Override
    public void getForecastDataFromServer(String date) {
        final String day = date.substring(8,10);

        if (!CheckConnectivity.checkNetworkConnection(context)) {
            view.showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);
        }
        else {
            Map<String, String> queryParams = RetrofitClient.setQueryParamsForForecast(date);
            Observable<ForecastList> forecastListObservable = apiService.getForecastData(queryParams);
            addDisposable(
                    forecastListObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ForecastList>() {
                            @Override
                            public void accept(@NonNull ForecastList forecastList) throws Exception {
                                List<Forecast> list = forecastList.getList();
                                if (list.isEmpty())
                                    view.showSnackBarMessage("예보정보를 찾지 못하였습니다.");
                                else {
                                    saveRecentForecastData(list, day);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.v(TAG, "Fail to get data from server[getForecastDataFromServer()] " + day);
                                view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                            }
                        })
            );
        }
    }



    private void saveRecentForecastData(List<Forecast> forecastRecent , String day) {
        Log.i(TAG, "# pref에 데이터 저장하기.");
        RecentForecast recentForecast = new RecentForecast();
        recentForecast.setSaveDay(day);

        recentForecast.setInformOverallToday_PM10(forecastRecent.get(0).getInformOverall());
        recentForecast.setPM10(forecastRecent.get(1));
        recentForecast.setImageUrl_PM10(forecastRecent.get(1).getImageUrl7());


        recentForecast.setInformOverallToday_PM25(forecastRecent.get(3).getInformOverall());
        recentForecast.setPM25(forecastRecent.get(4));
        recentForecast.setImageUrl_PM25(forecastRecent.get(4).getImageUrl8());


        recentForecast.setInformOverallToday_O3(forecastRecent.get(6).getInformOverall());
        recentForecast.setO3(forecastRecent.get(7));
        recentForecast.setImageUrl_O3(forecastRecent.get(7).getImageUrl9());

        pref.putObject(SharedPreferences.RECENT_DATA_FORECAST, recentForecast);

        view.saveDataToPreferences(recentForecast);
    }

    private int getTimePhaseZone(int hour) {
        if (hour >= 5 && hour < 11)
            return 0;
        else if (hour >= 11 && hour < 17)
            return 1;
        else if (hour >= 17 && hour < 23)
            return 2;

        return 3;
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
