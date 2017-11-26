package com.finedust.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.finedust.R;
import com.finedust.databinding.ActivityMainBinding;

import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.presenter.MainActivityPresenter;
import com.finedust.utils.AppSharedPreferences;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Views.MainActivityView{

    private static final String TAG = MainActivity.class.getSimpleName();

    AppSharedPreferences pref;
    ActivityMainBinding mainBinding;
    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setActivity(this);

        pref = new AppSharedPreferences(this);

        // ActionBar, DrawerLayout, Nav View
        setSupportActionBar(mainBinding.appBarMain.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainBinding.drawerLayout, mainBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mainActivityPresenter.onCreate();

        // Check widgetId and widgetMode. - in case of launching by clicking a widget.
        Intent intent = getIntent();
        Bundle mExtras = intent.getExtras();
        if (mExtras != null) {
            String selectedWidgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            if (selectedWidgetMode != null)
                pref.put(AppSharedPreferences.CURRENT_MODE, selectedWidgetMode);
        }

        // MODE Check & Instant run as AirConditionFragment
        String MODE = pref.getValue(AppSharedPreferences.CURRENT_MODE, Const.EMPTY_STRING);
        Log.i(TAG,  "checkCurrentMode : " + MODE);
        fragmentReplace(new AirConditionFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");

        checkNavigationForLocation();
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

        switch(id) {
            case R.id.nav_current:
                // Save the settings in SharedPreferences.
                pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[0]);
                fragmentReplace(new AirConditionFragment());
                break;

            case R.id.nav_loc_one:
                Log.i(TAG, "Location One Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_one).getTitle().equals("")) {
                    searchLocationIntent(1);
                }
                else {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[1]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_loc_two:
                Log.i(TAG, "Location Two Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_two).getTitle().equals("")) {
                    searchLocationIntent(2);
                }
                else {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[2]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_loc_three:
                Log.i(TAG, "Location Three Selected");
                if(mainBinding.navView.getMenu().findItem(R.id.nav_loc_three).getTitle().equals("")) {
                    searchLocationIntent(3);
                }
                else {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[3]);
                    fragmentReplace(new AirConditionFragment());
                }
                break;

            case R.id.nav_forecast:
                setToolbarBackgroundColor(0);
                fragmentReplace(new ForecastFragment());
                break;

            case R.id.nav_airkorea:
                setToolbarBackgroundColor(0);
                fragmentReplace(new WebPageAirKorea());

                break;
            case R.id.nav_kaq:
                setToolbarBackgroundColor(0);
                fragmentReplace(new WebpagesFragment());
                break;
            case R.id.nav_setting:
                setToolbarBackgroundColor(0);
                fragmentReplace(new SettingFragment());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Addresses saveLocation;

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            saveLocation = saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_One : " + saveLocation.getAddr());
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            saveLocation = saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_Two : " + saveLocation.getAddr());
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 3) {
            saveLocation = saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            Log.i(TAG, "Addr_Three : " + saveLocation.getAddr());
        }

        if ( (requestCode ==1 || requestCode == 2 || requestCode ==3) && resultCode == Activity.RESULT_OK ) {
            pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[requestCode]);
            fragmentReplace(new AirConditionFragment());
        }


    }

    public Addresses saveAddrInPreferences(int requestCode, Intent data, String key) {
        Addresses saveLocation  = new Addresses(data.getStringExtra("Addr"), data.getStringExtra("Umd"), data.getStringExtra("TmX"), data.getStringExtra("TmY"));
        pref.putObject(key, saveLocation);
        setNavigationTitle(saveLocation.getAddr(), requestCode, Const.NAVI_ICON_LOCATION_SAVED);

        return saveLocation;
    }


    // ------------ MainActivityView Interface --------------------

    @Override
    public void onFloatingButtonClick(View view) {
        Log.v(TAG, "onFloaotingButtonClick()");
        /*
        for(int i = 0;  i < 4; i++) {
            pref.put(SharedPreferences.CURRENT_MODE, Const.MODE[0]);
            pref.removeValue(SharedPreferences.RECENT_DATA[i]);
            pref.removeValue(SharedPreferences.MEMORIZED_LOCATIONS[i]);
            if(i != 0)
                setNavigationTitle(Const.EMPTY_STRING, i, Const.NAVI_ICON_LOCATION_NOT_SAVED);
        }

        checkNavigationForLocation();
        Snackbar.make(view, "All of preference data deleted.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        */
    }

    @Override
    public void fragmentReplace(Fragment newFragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, newFragment);

        transaction.commit();
    }

    @Override
    public void setNavigationTitle(String title, int position, int img) {
        switch (position) {
            case 1:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_one)
                        .setTitle(title)
                        .setIcon(img);
                break;
            case 2:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_two)
                        .setTitle(title)
                        .setIcon(img);
                break;
            case 3:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_three)
                        .setTitle(title)
                        .setIcon(img);
                break;
        }
    }

    @Override
    public void setNavigationChecked(int position, boolean check) {
        switch (position) {
            case 0:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_current)
                        .setChecked(check);
                break;
            case 1:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_one)
                        .setChecked(check);
                break;
            case 2:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_two)
                        .setChecked(check);
                break;
            case 3:
                mainBinding.navView.getMenu()
                        .findItem(R.id.nav_loc_three)
                        .setChecked(check);
                break;
        }
    }

    void checkNavigationForLocation() {
        for(int i = 1; i < 4; i++) {
            Addresses saved = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[i], "", new Addresses());
            if (saved != null) {
                setNavigationTitle(saved.getAddr() ,i, Const.NAVI_ICON_LOCATION_SAVED);
            }
        }
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Const.TOOLBAR_COLORS_DARK[color]);
            mainBinding.appBarMain.toolbar.setBackgroundColor(Const.TOOLBAR_COLORS[color]);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }
}
