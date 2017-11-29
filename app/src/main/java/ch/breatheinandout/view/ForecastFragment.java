package ch.breatheinandout.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ch.breatheinandout.R;
import ch.breatheinandout.databinding.FragmentForecastBinding;
import ch.breatheinandout.model.RecentForecast;
import ch.breatheinandout.model.adapter.ViewPagerAdapter;
import ch.breatheinandout.presenter.ForecastFragmentPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment implements View.OnClickListener , Views.ForecastFragmentView {
    private static final String TAG = ForecastFragment.class.getSimpleName();
    FragmentForecastBinding binding;
    ForecastFragmentPresenter forecastFragmentPresenter;
    ViewPagerAdapter mViewAdapter;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false);
        forecastFragmentPresenter = new ForecastFragmentPresenter(this, getContext());
        mViewAdapter = new ViewPagerAdapter(getFragmentManager());
        binding.viewPager.setAdapter(mViewAdapter);

        setOnClickListener();
        binding.btnPm10.setSelected(true);
        forecastFragmentPresenter.getForecastData();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        forecastFragmentPresenter.clearDisposable();
    }

    private void setOnClickListener() {
        binding.btnPm10.setOnClickListener(this);
        binding.btnPm25.setOnClickListener(this);
        binding.btnO3.setOnClickListener(this);
        binding.viewPager.addOnPageChangeListener(pageChangeListener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pm10:
                binding.viewPager.setCurrentItem(0);
                break;
            case R.id.btn_pm25:
                binding.viewPager.setCurrentItem(1);
                break;
            case R.id.btn_o3:
                binding.viewPager.setCurrentItem(2);
                break;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setAllButtonsUnselected();

            if (position == 0) {
                binding.btnPm10.setSelected(true);
            }
            else if (position == 1) {
                binding.btnPm25.setSelected(true);
            }
            else {
                binding.btnO3.setSelected(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setAllButtonsUnselected() {
        binding.btnPm10.setSelected(false);
        binding.btnPm25.setSelected(false);
        binding.btnO3.setSelected(false);
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }


    public void showSnackBarMessage(String msg) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, 3000).show();
    }

    @Override
    public void saveDataToPreferences(RecentForecast recentForecast) {
        Log.i(TAG, "saveDataToPreferences() - Fragment");
        //binding.viewPager.setCurrentItem(0);
        resetViewpagerAdapter();
    }
    private void resetViewpagerAdapter() {
        try {
            binding.viewPager.setAdapter(mViewAdapter);
        }
        catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }
}
