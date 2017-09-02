package com.finedust.presenter;

import android.content.Context;

/**
 * Created by CH on 2017-09-02.
 */

public interface Presenter {

    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();
    void onSampleButtonClicked();

    void getAirConditionData(Context context, String stationName);



}
