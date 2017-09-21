package com.finedust.presenter;

import android.content.Context;

import com.finedust.model.GpsData;
import com.finedust.model.StationList;

interface Presenter {

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

    interface SearchAddressActivityPresenter {
        void getAddressData(Context context, String str);
    }

    interface ForecastFragmentPresenter {

    }

    interface SettingFragmentPresenter {
        void abc();
    }




}
