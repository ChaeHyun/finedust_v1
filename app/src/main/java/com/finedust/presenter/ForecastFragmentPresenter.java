package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.Const;
import com.finedust.model.Forecast;
import com.finedust.model.ForecastList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.Views;

import java.text.DateFormat;
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
import retrofit2.Retrofit;

public class ForecastFragmentPresenter implements Presenter.ForecastFragmentPresenter {
    private static final String TAG = ForecastFragmentPresenter.class.getSimpleName();
    private Views.ForecastFragmentView[] view = new Views.ForecastFragmentView[3];
    private Context context;
    private int index;

    private SharedPreferences pref;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    public ForecastFragmentPresenter(Views.ForecastFragmentView view, int index, Context context) {
        this.view[index] = view;
        this.index = index;
        this.context = context;
        pref = new SharedPreferences(context);
        apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getForecastData(final int index) {
        Log.i(TAG, "getForecastData(" + index + ")");
        String date = calculateCurrentDate();

        if (!CheckConnectivity.checkNetworkConnection(context)) {
            view[index].showSnackBarMessage(Const.STR_NETWORK_NOT_AVAILABLE);
        }
        else if (date != null) {
            Map<String, String> queryParams = RetrofitClient.setQueryParamsForForecast(date, Const.FORECAST_INFORM_CODE[index]);
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
                                    view[0].showSnackBarMessage("예보정보를 찾지 못하였습니다.");
                                else {
                                    Forecast data = list.get(0);

                                    Log.i(TAG, data.toString());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.v(TAG, "Fail to get data from server[getForecastData())]");
                                view[index].showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                            }
                        })
            );
        }
    }

    private String calculateCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar mCalendar = Calendar.getInstance();
        String date = null;

        int hour = Integer.parseInt(String.valueOf(mCalendar.get(Calendar.HOUR_OF_DAY)));
        if (hour >= 0 && hour < 5) {
            // 다음 날 정보는 05:00에 최초 업데이트 된다.
            mCalendar.add(Calendar.DATE, -1);
            date = dateFormat.format(mCalendar.getTime());
        }
        else {
            date = dateFormat.format(mCalendar.getTime());
        }

        Log.i(TAG, "시간 : " + date);
        return date;
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
