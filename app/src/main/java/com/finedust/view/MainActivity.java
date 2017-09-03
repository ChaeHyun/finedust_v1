package com.finedust.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityView{

    private static final String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mainBinding;
    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);

    private MyAdapter adapter;

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


        // test : DataBinding 이용해서 TextView setText
        mainBinding.appBarMain.contentMain.button.setText("버튼");

        // 리스트뷰 아이템 클릭 -> ## Databinding 이용해서 분리할 방법 찾아보기.
        mainBinding.appBarMain.contentMain.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(adapter != null) {
                    Toast.makeText(getApplicationContext(),
                            "PM10 Value : " + adapter.getItem(position).getPm10Value()
                                    + "\nCO Value : " + adapter.getItem(position).getCoValue()
                                    + "\nSO2 Value : " + adapter.getItem(position).getSo2Value()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        mainActivityPresenter.onCreate();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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


        mainActivityPresenter.getAirConditionData(getApplicationContext(), temporaryStationName);
    }

    @Override
    public void onSampleButtonClick(View view) {
        Log.i(TAG, "onSamplelButtonoClick()");
        mainBinding.appBarMain.contentMain.button.setText("Changed");

        //Presenter를 이용해서 airConditionPresenter의 onSampleButtonClicked() 메소드를 호출.
        //프리젠터에 호출을 요청하고 난 뒤에 역할 끝.

        // .onSampleButtonClicked() 내부에서 Business Logic을 처리한 후 View에게 업데이트를 요청. (view.showTestToastMessage)
        mainActivityPresenter.onSampleButtonClicked();
    }

    /**
     * 프리젠터에서 요청 시에 뷰에 업데이트 작업을 수행하는 메소드.
     * */

    @Override
    public void showTestToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAirConditionData(ArrayList<AirCondition> data) {
        Log.i(TAG, "updating Air Condition Data to List Adapter");
        adapter = new MyAdapter(MainActivity.this, 0, data);
        mainBinding.appBarMain.contentMain.listView.setAdapter(adapter);
    }
}
