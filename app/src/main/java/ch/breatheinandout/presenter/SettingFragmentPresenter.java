package ch.breatheinandout.presenter;

import android.content.Context;

import ch.breatheinandout.utils.AppSharedPreferences;
import ch.breatheinandout.view.Views;


public class SettingFragmentPresenter implements Presenter.SettingFragmentPresenter {
    private static final String TAG = SettingFragmentPresenter.class.getSimpleName();
    private Views.SettingFragmentView view;
    private Context context;

    private AppSharedPreferences pref;

    public SettingFragmentPresenter(Views.SettingFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        pref = new AppSharedPreferences(context);
    }

    @Override
    public void abc() {

    }

    @Override
    public void deleteSavedData() {
        for (int i = 0; i < AppSharedPreferences.RECENT_DATA.length; i++)
            pref.removeValue(AppSharedPreferences.RECENT_DATA[i]);
    }
}
