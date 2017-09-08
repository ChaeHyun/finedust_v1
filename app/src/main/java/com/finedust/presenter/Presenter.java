package com.finedust.presenter;

import android.content.Context;

public interface Presenter {

    interface MainActivityPresenter {
        void onCreate();
        void onPause();
        void onResume();
        void onDestroy();

    }

    interface AirConditionFragmentPresenter {
        void onSampleButtonClicked();
        void getAirConditionData(Context context, String stationName);
        //void getGpsCoordinates(Context context);

    }

    interface SearchAddressActivityPresenter {
        void getAddressData(Context context, String str);
    }

    interface ForecastFragmentPresenter {

    }




}
