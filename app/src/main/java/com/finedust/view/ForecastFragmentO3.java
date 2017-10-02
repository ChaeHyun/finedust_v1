package com.finedust.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.finedust.R;
import com.finedust.databinding.ForecastUiBinding;
import com.finedust.model.Const;
import com.finedust.model.RecentForecast;
import com.finedust.utils.SharedPreferences;


public class ForecastFragmentO3 extends Fragment {
    private static final String TAG = ForecastFragmentO3.class.getSimpleName();

    ForecastUiBinding binding;
    SharedPreferences pref;


    public ForecastFragmentO3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.forecast_ui, container, false);
        pref = new SharedPreferences(getContext());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateDataToViews();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateDataToViews() {
        try {
            RecentForecast recentForecast = (RecentForecast) pref.getObject(SharedPreferences.RECENT_DATA_FORECAST, Const.EMPTY_STRING, new RecentForecast());
            binding.layoutInfoZero.textDate.setText(recentForecast.getO3().getDataTime());
            binding.layoutInfoZero.textContentZero.setText(recentForecast.getInformOverallToday_O3());
            binding.layoutInfo.textContentOne.setText(recentForecast.getO3().getInformOverall());
            binding.layoutReason.textContentTwo.setText(recentForecast.getO3().getInformCause());
            binding.layoutStates.textContentThree.setText(recentForecast.getO3().getInformGrade() + "\n" + recentForecast.getImageUrl_O3());

            binding.layoutInfo.imageLayout.setVisibility(View.VISIBLE);
            GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(binding.layoutInfo.imgYebo);
            Glide.with(getContext()).load(recentForecast.getImageUrl_O3()).dontTransform().dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).into(gifImage);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
    }
}
