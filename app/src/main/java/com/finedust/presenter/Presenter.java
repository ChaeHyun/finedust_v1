package com.finedust.presenter;

import android.content.Context;

interface Presenter {

    interface MainActivityPresenter {
        void onCreate();
        void onPause();
        void onResume();
        void onDestroy();

    }

    interface AirConditionFragmentPresenter {
        void onPause();
        void getNearStationList(String x, String y);
        void getAirConditionData(Context context, String stationName);
        void getGPSCoordinates();
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
