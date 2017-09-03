package com.finedust.view;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.ActivityMainBinding;

import com.finedust.model.AirCondition;
import com.finedust.model.adapter.MyAdapter;
import com.finedust.presenter.MainActivityPresenter;
import com.finedust.presenter.Presenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Views.MainActivityView{

    private static final String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mainBinding;
    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setActivity(this);

        // ActionBar, DrawerLayout, Nav View
        setSupportActionBar(mainBinding.appBarMain.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainBinding.drawerLayout, mainBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mainBinding.navView.setNavigationItemSelectedListener(this);
        mainActivityPresenter.onCreate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void onBackPressed() {

        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast t = new Toast(getApplicationContext());

        if (id == R.id.nav_current) {
            // Save the settings in SharedPreferences.
            Fragment airConditionFragment = new AirConditionFragment();
            fragmentReplace(airConditionFragment);

        } else if (id == R.id.nav_loc_one) {
            if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_one).getTitle().equals("")) {
                // Save New Address
            }
            else {
                Fragment airConditionFragment = new AirConditionFragment();
                fragmentReplace(airConditionFragment);
            }

        } else if (id == R.id.nav_loc_two) {

        } else if (id == R.id.nav_loc_three) {

        } else if (id == R.id.nav_forecast) {
            Fragment forecastFragment = new ForecastFragment();
            fragmentReplace(forecastFragment);
            t.makeText(getApplicationContext(), "예보정보 선택", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_airkorea) {

        } else if (id == R.id.nav_kaq) {

        } else if (id == R.id.nav_nullschool) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ------------ MainActivityView Interface --------------------

    @Override
    public void onFloatingButtonClick(View view) {
        final String temporaryStationName = "호림동";

        Log.v(TAG, "onFloaotingButtonClick()");
        Snackbar.make(view, temporaryStationName + "의 대기정보를 가져옵니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void fragmentReplace(Fragment newFragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, newFragment);

        transaction.commit();
    }

    @Override
    public void setNavigationTitle(String title, int position) {
        switch(position) {
            case 0:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_one)
                        .setTitle(title)
                        .setIcon(R.drawable.pin_128);
                break;
            case 1:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_two)
                        .setTitle(title)
                        .setIcon(R.drawable.pin_128);
                break;
            case 2:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_three)
                        .setTitle(title)
                        .setIcon(R.drawable.pin_128);
                break;
        }
    }
}
