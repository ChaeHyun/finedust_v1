package com.finedust.presenter;

import android.content.Context;

import com.finedust.model.Const;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.Views;


public class SettingFragmentPresenter implements Presenter.SettingFragmentPresenter {
    private static final String TAG = SettingFragmentPresenter.class.getSimpleName();
    private Views.SettingFragmentView view;
    private Context context;

    SharedPreferences pref;

    public SettingFragmentPresenter(Views.SettingFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        pref = new SharedPreferences(context);
    }

    @Override
    public void abc() {

    }

    @Override
    public void deleteSavedData() {
        pref.put(SharedPreferences.CURRENT_MODE, Const.MODE[0]);
        for (int i=0; i < SharedPreferences.RECENT_DATA.length; i++)
            pref.removeValue(SharedPreferences.RECENT_DATA[i]);
    }
}
