package com.finedust.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finedust.R;
import com.finedust.databinding.FragmentForecastBinding;
import com.finedust.model.adapter.ViewPagerAdapter;
import com.finedust.presenter.ForecastFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment implements Views.ForecastFragmentView , View.OnClickListener{
    private static final String TAG = ForecastFragment.class.getSimpleName();
    FragmentForecastBinding binding;
    //ForecastFragmentPresenter forecastFragmentPresenter;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false);
        //forecastFragmentPresenter = new ForecastFragmentPresenter(this, getContext());

        setOnClickListener();

        ViewPagerAdapter mViewAdapter = new ViewPagerAdapter(getFragmentManager());

        binding.btnPm10.setSelected(true);
        binding.viewPager.setAdapter(mViewAdapter);

        return binding.getRoot();
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

    }

    @Override
    public void showSnackBarMessage(String msg) {

    }
}
