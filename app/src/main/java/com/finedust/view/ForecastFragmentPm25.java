package com.finedust.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.ForecastUiBinding;
import com.finedust.presenter.ForecastFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragmentPm25 extends Fragment implements Views.ForecastFragmentView {
    private static final String TAG = ForecastFragmentPm25.class.getSimpleName();

    ForecastUiBinding binding;
    ForecastFragmentPresenter forecastFragmentPresenter;


    public ForecastFragmentPm25() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        forecastFragmentPresenter.clearDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.forecast_ui, container, false);
        forecastFragmentPresenter = new ForecastFragmentPresenter(this, 1, getContext());

        forecastFragmentPresenter.getForecastData(1);

        return binding.getRoot();
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), "PM25\n" + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, 3000).show();
    }
}
