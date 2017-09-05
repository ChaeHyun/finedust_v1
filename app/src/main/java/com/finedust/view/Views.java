package com.finedust.view;

import android.support.v4.app.Fragment;
import android.view.View;

import com.finedust.model.Address;
import com.finedust.model.AirCondition;

import java.util.ArrayList;

/**
 * Created by CH on 2017-09-03.
 */

public interface Views {

    interface MainActivityView {
        void onFloatingButtonClick(View view);
        void fragmentReplace(Fragment newFragment);
        void setNavigationTitle(String title, int position);
    }

    interface AirConditionFragmentView {
        void showToastMessage(String msg);
        void onSampleButtonClick(View view);
        void updateAirConditionData(ArrayList<AirCondition> data);
    }

    interface ForecastFragmentView {

    }

    interface SearchAddressActivityView {
        void showToastMessage(String msg);
        void updateAddressData(ArrayList<Address> data);
    }
}
