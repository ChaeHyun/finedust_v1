package ch.breatheinandout.presenter;

import android.util.Log;

import ch.breatheinandout.view.Views;


public class MainActivityPresenter implements Presenter.MainActivityPresenter {
    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    private Views.MainActivityView view;

    public MainActivityPresenter(Views.MainActivityView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
