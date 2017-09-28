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


public class ForecastFragmentO3 extends Fragment implements Views.ForecastFragmentView{
    private static final String TAG = ForecastFragmentO3.class.getSimpleName();

    ForecastUiBinding binding;
    ForecastFragmentPresenter  forecastFragmentPresenter;

    public ForecastFragmentO3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.forecast_ui, container, false);
        forecastFragmentPresenter = new ForecastFragmentPresenter(this, 2, getContext());

        forecastFragmentPresenter.getForecastData(2);

        return binding.getRoot();
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, 3000).show();
    }
}
