package com.finedust.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.ActivityMainBinding;

import com.finedust.model.Const;
import com.finedust.model.pref.MemorizedAddress;
import com.finedust.presenter.MainActivityPresenter;
import com.finedust.utils.SharedPreferences;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Views.MainActivityView{

    private static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences pref;
    ActivityMainBinding mainBinding;
    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setActivity(this);

        pref = new SharedPreferences(this);

        // ActionBar, DrawerLayout, Nav View
        setSupportActionBar(mainBinding.appBarMain.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainBinding.drawerLayout, mainBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mainActivityPresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");

        checkMemorizedAddresses();
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
        mainBinding.navView.setNavigationItemSelectedListener(this);

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

    @Override
    public void searchLocationIntent(int requestCode) {
        Intent intent = new Intent(this, SearchAddressActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast t = new Toast(getApplicationContext());

        switch(id) {
            case R.id.nav_current:
                // Save the settings in SharedPreferences.
                pref.put(Const.CURRENT_MODE, Const.MODE[0]);
                fragmentReplace(new AirConditionFragment());
                break;

            case R.id.nav_loc_one:
                Log.i(TAG, "Location One Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_one).getTitle().equals("")) {
                    searchLocationIntent(0);
                }
                else {
                    pref.put(Const.CURRENT_MODE, Const.MODE[1]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_loc_two:
                Log.i(TAG, "Location Two Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_two).getTitle().equals("")) {
                    searchLocationIntent(1);
                }
                else {
                    pref.put(Const.CURRENT_MODE, Const.MODE[2]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_loc_three:
                Log.i(TAG, "Location Three Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_three).getTitle().equals("")) {
                    searchLocationIntent(2);
                }
                else {
                    pref.put(Const.CURRENT_MODE, Const.MODE[3]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_forecast:
                Fragment forecastFragment = new ForecastFragment();
                fragmentReplace(forecastFragment);
                t.makeText(getApplicationContext(), "예보정보 선택", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_airkorea:
                break;
            case R.id.nav_kaq:
                break;
            case R.id.nav_setting:
                Fragment settingFragment = new SettingFragment();
                fragmentReplace(settingFragment);
                t.makeText(getApplicationContext(), "설정화면 선택", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MemorizedAddress saveLocation;

        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            saveLocation = saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_One : " + saveLocation.getMemorizedAddress());

            Fragment airCondition = new AirConditionFragment();
            fragmentReplace(airCondition);
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            saveLocation = saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_Two : " + saveLocation.getMemorizedAddress());

            Fragment airCondition = new AirConditionFragment();
            fragmentReplace(airCondition);
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            saveLocation = saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_Three : " + saveLocation.getMemorizedAddress());


            fragmentReplace(new AirConditionFragment());
        }

    }

    public MemorizedAddress saveAddrInPreferences(int requestCode, Intent data, String key) {
        MemorizedAddress saveLocation = new MemorizedAddress(data.getStringExtra("Addr"), data.getStringExtra("Umd"), data.getStringExtra("TmX"), data.getStringExtra("TmY"));
        pref.putObject(key, saveLocation);
        setNavigationTitle(saveLocation.getMemorizedAddress(), requestCode, Const.NAVI_ICON_LOCATION_SAVED);

        return saveLocation;
    }


    // ------------ MainActivityView Interface --------------------

    @Override
    public void onFloatingButtonClick(View view) {

        Log.v(TAG, "onFloaotingButtonClick()");
        Snackbar.make(view, "저장된 주소를 초기화 합니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void fragmentReplace(Fragment newFragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, newFragment);

        transaction.commit();
    }

    @Override
    public void setNavigationTitle(String title, int position, int img) {
        switch(position) {
            case 0:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_one)
                        .setTitle(title)
                        .setIcon(img);
                break;
            case 1:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_two)
                        .setTitle(title)
                        .setIcon(img);
                break;
            case 2:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_three)
                        .setTitle(title)
                        .setIcon(img);
                break;
        }
    }

    void checkMemorizedAddresses() {
        for(int i = 0; i < 3; i++) {
            MemorizedAddress save = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[i], "", new MemorizedAddress());
            if (save != null) {
                setNavigationTitle(save.getMemorizedAddress() ,i, Const.NAVI_ICON_LOCATION_SAVED);
            }
        }
    }

}
