package com.finedust.presenter;

import android.content.Context;

import com.finedust.model.GpsData;
import com.finedust.model.StationList;

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

    interface AirConditionFragmentPresenter {
        void onPause();
        void getNearStationList(final String x, final String y);
        void getAirConditionData(final StationList stationList, final int index, final GpsData gps);
        void getGPSCoordinates();

        void checkCurrentMode(String mode);
        int convertModeToInteger(final String mode);
    }

    interface SearchAddressActivityPresenter extends BasePresenter {
        void getAddressData(Context context, String str);
    }

    interface ForecastFragmentPresenter {

    }

    interface SettingFragmentPresenter {
        void abc();
    }




}
