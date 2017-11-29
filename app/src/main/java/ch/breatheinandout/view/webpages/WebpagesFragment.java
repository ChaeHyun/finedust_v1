package ch.breatheinandout.view.webpages;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.breatheinandout.R;
import ch.breatheinandout.databinding.FragmentWebsitesBinding;
import ch.breatheinandout.model.adapter.WebpageViewPagerAdapter;

public class WebpagesFragment extends Fragment {
    private static final String TAG = WebpagesFragment.class.getSimpleName();
    FragmentWebsitesBinding binding;
    WebpageViewPagerAdapter mViewAdapter;

    public WebpagesFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_websites, container, false);
        mViewAdapter = new WebpageViewPagerAdapter(getFragmentManager());
        mViewAdapter.addFragment("AQI", new WebPageAQI());
        mViewAdapter.addFragment("nullschool.net", new WebPageNullSchool());
        mViewAdapter.addFragment("한국대기질예보", new WebPageKaq());
        mViewAdapter.addFragment("tenki.jp", new WebPageTenki());
        binding.viewpager.setAdapter(mViewAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);

        return binding.getRoot();
    }
}
