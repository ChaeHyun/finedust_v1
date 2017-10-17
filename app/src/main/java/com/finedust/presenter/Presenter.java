package com.finedust.presenter;

import android.content.Context;

import io.reactivex.disposables.Disposable;

interface Presenter {

    interface BasePresenter {
        void addDisposable(Disposable disposable);
        void clearDisposable(); // must be called before the activity is destroyed.
    }

    interface MainActivityPresenter {
        void onCreate();
        void onPause();
        void onResume();
        void onDestroy();

    }

    interface AirConditionFragmentPresenter extends BasePresenter{
        void onPause();
        void getAirConditionData(final String x, final String y);
        void getGPSCoordinates();
        void checkCurrentMode(String mode);
        int convertModeToInteger(final String mode);
    }

    interface SearchAddressActivityPresenter extends BasePresenter {
        void getAddressData(Context context, String str);
    }

    interface ForecastFragmentPresenter extends BasePresenter {
        void getForecastDataFromServer(String date);
        void getForecastData();
    }

    interface SettingFragmentPresenter {
        void abc();
        void deleteSavedData();
    }


}
