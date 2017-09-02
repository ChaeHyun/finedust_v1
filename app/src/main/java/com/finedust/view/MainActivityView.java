package com.finedust.view;

import android.view.View;

import com.finedust.model.AirCondition;

import java.util.ArrayList;

/**
 * Created by CH on 2017-09-02.
 */

public interface MainActivityView {
    void onFloatingButtonClick(View view);
    void onSampleButtonClick(View view);
    void showTestToastMessage(String msg);

    void updateAirConditionData(ArrayList<AirCondition> data);

}
