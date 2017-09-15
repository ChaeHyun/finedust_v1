package com.finedust.presenter;

import android.content.Context;

import com.finedust.view.Views;


public class SettingFragmentPresenter implements Presenter.SettingFragmentPresenter {
    private static final String TAG = SettingFragmentPresenter.class.getSimpleName();
    private Views.SettingFragmentView view;
    private Context context;

    public SettingFragmentPresenter(Views.SettingFragmentView view) {
        this.view = view;
    }

    @Override
    public void abc() {

    }
}
