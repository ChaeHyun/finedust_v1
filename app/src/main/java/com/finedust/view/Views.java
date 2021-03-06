package com.finedust.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.finedust.model.Addresses;
import com.finedust.model.AirCondition;
import com.finedust.model.Forecast;
import com.finedust.model.RecentData;
import com.finedust.model.RecentForecast;

import java.util.List;

public interface Views {

    interface BaseView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
    }

    interface MainActivityView {
        void onFloatingButtonClick(View view);
        void fragmentReplace(Fragment newFragment);
        Addresses saveAddrInPreferences(int requestCode, Intent data, String key);
        void setNavigationTitle(String title, int position, int img);
        void setNavigationChecked(int position, boolean check);
        void searchLocationIntent(int requestCode);
        void setToolbarBackgroundColor(int color);
    }

    interface AirConditionFragmentView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
        void updateDataToViews(RecentData recnetData);
        boolean checkPermission();
        void checkGpsEnabled();
    }

    interface ForecastFragmentView extends BaseView {
        void saveDataToPreferences(RecentForecast recentForecast);

    }

    interface SearchAddressActivityView {
        void showToastMessage(String msg);
        void showSnackBarMessage(String msg);
        void enableNetworkOptions();
        void updateAddressData(List<Addresses> data);
    }

    interface ChangeGradeActivityView {

    }

    interface SettingFragmentView {

    }
}
