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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

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

        String currentDay = dateFormat.format(mCalendar.getTime()).substring(8,10);

        if (!compareSavedDataTime(currentDay, String.valueOf(currentHour))) {
            date = dateFormat.format(mCalendar.getTime());

            return date;
        }

        return null;
    }


    private boolean compareSavedDataTime(String currentDay , String currentHour) {
        try {
            RecentForecast recentForecast = (RecentForecast) pref.getObject(SharedPreferences.RECENT_DATA_FORECAST, Const.EMPTY_STRING, new RecentForecast());

            if (String.valueOf(currentDay).equals(recentForecast.getSaveDay())) {
                int pastHour = Integer.parseInt(recentForecast.getPM10().getDataTime().substring(11,13));

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
                                Log.v(TAG, "Fail to get data from server[getForecastDataFromServer()] ");
                                view.showSnackBarMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                            }
                        })
            );
        }
    }



    private void saveRecentForecastData(List<Forecast> forecastRecent , String day) {
        RecentForecast recentForecast = new RecentForecast();
        recentForecast.setSaveDay(day);

        forecastRecent.get(1).setInformGrade(setStringArrange(forecastRecent.get(1).getInformGrade()));
        recentForecast.setInformOverallToday_PM10(forecastRecent.get(0).getInformOverall());
        recentForecast.setInformCause_PM10(forecastRecent.get(0).getInformCause());
        recentForecast.setPM10(forecastRecent.get(1));
        recentForecast.setImageUrl_PM10(forecastRecent.get(1).getImageUrl7());

        forecastRecent.get(4).setInformGrade(setStringArrange(forecastRecent.get(4).getInformGrade()));
        recentForecast.setInformOverallToday_PM25(forecastRecent.get(3).getInformOverall());
        recentForecast.setInformCause_PM25(forecastRecent.get(3).getInformCause());
        recentForecast.setPM25(forecastRecent.get(4));
        recentForecast.setImageUrl_PM25(forecastRecent.get(4).getImageUrl8());

        try {
            forecastRecent.get(7).setInformGrade(setStringArrange(forecastRecent.get(7).getInformGrade()));
            recentForecast.setInformOverallToday_O3(forecastRecent.get(6).getInformOverall());
            recentForecast.setInformCause_O3(forecastRecent.get(6).getInformCause());
            recentForecast.setO3(forecastRecent.get(7));
            recentForecast.setImageUrl_O3(forecastRecent.get(7).getImageUrl9());
        }
        catch (IndexOutOfBoundsException e) {
            Log.i(TAG, "No Data for O3");
        }

        pref.putObject(SharedPreferences.RECENT_DATA_FORECAST, recentForecast);

        view.saveDataToPreferences(recentForecast);
    }


    private String setStringArrange(String str) {
        if (str.equals(""))
            return "";

        String result = null;
        ArrayList<String> cityGradeList = new ArrayList<>();
        StringTokenizer token = new StringTokenizer(str, ",");
        int cntToken = token.countTokens();
        for (int i = 0; i < cntToken; i++) {
            String divided = token.nextToken();
            cityGradeList.add(divided);
        }

        if (!cityGradeList.isEmpty()) {
            String temp = cityGradeList.get(15);
            cityGradeList.set(15, cityGradeList.get(17));
            cityGradeList.set(17, temp);

            if (cityGradeList.get(0).equals("서울 : 예보없음")) {
                result = "○[시도별등급] 현재 예보정보 없음";
            }
            else {
                result = "[ " + cityGradeList.get(0);
                for (int i = 1; i < cityGradeList.size(); i++) {
                    if ( (i%3 == 0 && i != 18) || i == 17)
                        result += " ]\n[ " + cityGradeList.get(i);
                    else
                        result += " ] [" + cityGradeList.get(i);
                }
                result += " ]";
            }
        }

        return result;
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
