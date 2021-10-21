package ch.breatheinandout.model.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.breatheinandout.view.ForecastFragmentO3;
import ch.breatheinandout.view.ForecastFragmentPm10;
import ch.breatheinandout.view.ForecastFragmentPm25;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    private Fragment[] fragments = new Fragment[3];

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new ForecastFragmentPm10();
        fragments[1] = new ForecastFragmentPm25();
        fragments[2] = new ForecastFragmentO3();
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }
}
