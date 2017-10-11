package com.finedust.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;

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
        if (checkAvailableTime()) {
            try {
                RecentForecast recentForecast = (RecentForecast) pref.getObject(SharedPreferences.RECENT_DATA_FORECAST, Const.EMPTY_STRING, new RecentForecast());
                binding.layoutInfoZero.textDate.setText(recentForecast.getO3().getDataTime());
                binding.layoutInfoZero.textContentZero.setText(recentForecast.getInformOverallToday_O3());
                binding.layoutInfo.textContentOne.setText(recentForecast.getO3().getInformOverall());
                binding.layoutReason.textContentTwo.setText(recentForecast.getInformCause_O3());
                binding.layoutStates.textContentThree.setText(recentForecast.getO3().getInformGrade());

                binding.layoutReason.imageLayout.setVisibility(View.VISIBLE);
                GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(binding.layoutReason.imgYebo);
                Glide.with(getContext()).load(recentForecast.getImageUrl_O3()).dontTransform().dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).into(gifImage);
            }
            catch (NullPointerException e) {
                //e.printStackTrace();
            }
        }
        else {
            binding.layoutInfoZero.textDate.setText("비운영기간");
            binding.layoutInfoZero.textContentZero.setText("오존 예보는 매년 4월15일 ~ 10월15일까지 발표됩니다.");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
    }

    private boolean checkAvailableTime() {
        //04.15 ~ 10.15
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH, Calendar.APRIL);
        mCalendar.set(Calendar.DATE, 15);
        int start = mCalendar.get(Calendar.DAY_OF_YEAR);

        mCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        mCalendar.set(Calendar.DATE, 15);
        int end = mCalendar.get(Calendar.DAY_OF_YEAR);


        Calendar today = Calendar.getInstance();
        int dayOfYear = today.get(Calendar.DAY_OF_YEAR);

        return (start <= dayOfYear && dayOfYear <= end);
    }
}
