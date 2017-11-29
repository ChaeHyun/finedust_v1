package ch.breatheinandout.view;


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
import ch.breatheinandout.R;
import ch.breatheinandout.databinding.ForecastUiBinding;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.model.RecentForecast;
import ch.breatheinandout.utils.AppSharedPreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragmentPm10 extends Fragment {
    private static final String TAG = ForecastFragmentPm10.class.getSimpleName();

    ForecastUiBinding binding;
    AppSharedPreferences pref;

    public ForecastFragmentPm10() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.forecast_ui, container, false);
        pref = new AppSharedPreferences(getContext());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        updateDataToViews();
    }

    private void updateDataToViews() {
        try {
            RecentForecast recentForecast = (RecentForecast) pref.getObject(AppSharedPreferences.RECENT_DATA_FORECAST, Const.EMPTY_STRING, new RecentForecast());
            binding.layoutInfoZero.textDate.setText(recentForecast.getPM10().getDataTime());
            binding.layoutInfoZero.textContentZero.setText(recentForecast.getInformOverallToday_PM10());
            binding.layoutInfo.textContentOne.setText(recentForecast.getPM10().getInformOverall());
            binding.layoutReason.textContentTwo.setText(recentForecast.getInformCause_PM10());
            binding.layoutStates.textContentThree.setText(recentForecast.getPM10().getInformGrade());

            binding.layoutReason.imageLayout.setVisibility(View.VISIBLE);
            GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(binding.layoutReason.imgYebo);
            Glide.with(getContext()).load(recentForecast.getImageUrl_PM10()).dontTransform().dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).into(gifImage);

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
