package com.finedust.view;

import android.support.v4.app.Fragment;
import android.view.View;

import com.finedust.model.Address;
import com.finedust.model.AirCondition;

import java.util.ArrayList;

public interface Views {

    interface MainActivityView {
        void onFloatingButtonClick(View view);
        void fragmentReplace(Fragment newFragment);
        void setNavigationTitle(String title, int position);
    }

    interface AirConditionFragmentView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
        void onSampleButtonClick(View view);
        void updateAirConditionData(ArrayList<AirCondition> data);
        //void getGpsCoordinates();
        boolean checkPermission();
        void checkGpsEnabled();
    }

    interface ForecastFragmentView {

    }

    interface SearchAddressActivityView {
        void showToastMessage(String msg);
        void enableNetworkOptions();
        void updateAddressData(ArrayList<Address> data);
    }
}
