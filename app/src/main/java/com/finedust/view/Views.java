package com.finedust.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.finedust.model.Addresses;
import com.finedust.model.AirCondition;


import java.util.ArrayList;

public interface Views {

    interface MainActivityView {
        void onFloatingButtonClick(View view);
        void fragmentReplace(Fragment newFragment);
        Addresses saveAddrInPreferences(int requestCode, Intent data, String key);
        void setNavigationTitle(String title, int position, int img);
        void searchLocationIntent(int requestCode);
    }

    interface AirConditionFragmentView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
        void onSampleButtonClick(View view);
        void updateAirConditionData(ArrayList<AirCondition> data);
        boolean checkPermission();
        void checkGpsEnabled();

    }

    interface ForecastFragmentView {

    }

    interface SearchAddressActivityView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
        void enableNetworkOptions();
        void updateAddressData(ArrayList<Addresses> data);
    }

    interface SettingFragmentView {

    }
}
