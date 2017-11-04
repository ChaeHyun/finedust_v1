package com.finedust.view;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.FragmentAirConditionBinding;
import com.finedust.model.AirCondition;

import com.finedust.model.Const;
import com.finedust.model.RecentData;


import com.finedust.model.Station;
import com.finedust.presenter.AirConditionFragmentPresenter;
import com.finedust.utils.CheckConnectivity;
import com.finedust.utils.AppSharedPreferences;


public class AirConditionFragment extends Fragment implements Views.AirConditionFragmentView {
    private static final String TAG = AirConditionFragment.class.getSimpleName();

    FragmentAirConditionBinding  binding;
    AirConditionFragmentPresenter airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());
    Views.MainActivityView mainView;
    AppSharedPreferences pref;

    final int MY_PERMISSION_REQUEST_LOCATION = 1000;
    boolean isPermissionEnabled = false;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private String selectedWidgetMode = null;
    private String widgetThemeMode = null;
    private String widgetThemeAction = null;


    public AirConditionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_air_condition, container, false);
        binding.setAircondition(this);

        pref = new AppSharedPreferences(getActivity());

        airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());
        mainView = (MainActivity) getActivity();

        // Check widgetId and widgetMode. - in case of launching by clicking a widget.
        Intent intent = getActivity().getIntent();
        Bundle mExtras = intent.getExtras();

        if (mExtras != null) {
            mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            selectedWidgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            if (selectedWidgetMode != null) {
                //pref.put(AppSharedPreferences.CURRENT_MODE, selectedWidgetMode);
                widgetThemeMode = intent.getStringExtra(Const.WIDGET_THEME);
                widgetThemeAction = getWidgetThemeAction(widgetThemeMode);
                Log.i(TAG, "widgetMode : " + selectedWidgetMode + " , AppSharedPreferences.CURRENT_MODE : " + pref.getValue(AppSharedPreferences.CURRENT_MODE, Const.EMPTY_STRING));
            }
        }

        // launch at start, but not resume;
        String MODE = pref.getValue(AppSharedPreferences.CURRENT_MODE, Const.MODE[0]);
        Log.i(TAG, " checkCurrentMode  : " + MODE);
        mainView.setNavigationChecked(airConditionFragmentPresenter.convertModeToInteger(MODE), true);
        airConditionFragmentPresenter.checkCurrentMode(MODE);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        airConditionFragmentPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        airConditionFragmentPresenter.clearDisposable();
        super.onDestroy();
    }

    @Override
    public void updateDataToViews(RecentData recentData) {
        try {
            AirCondition air = recentData.getAirCondition().get(0);
            Station weatherStation = recentData.getSavedStations().get(0);

            binding.layoutLocationInfo.textContentLocation.setText(recentData.getAddr().getAddr());
            setAllAirConditionData(air);
            binding.layoutStations.textContentWeathercenter.setText(weatherStation.toString());

            // 받은 데이터를 이용해서 위젯의 정보도 업데이트.
            Log.i(TAG, "RECENT MODE : " + recentData.getCurrentMode());
            Log.i(TAG, "WIDGET SELECT : " + selectedWidgetMode);
            if (recentData.getCurrentMode().equals(selectedWidgetMode)) {
                Log.i(TAG, "App -> Widget Update");
                airConditionFragmentPresenter.sendResponseToWidget(recentData, mAppWidgetId, widgetThemeMode, widgetThemeAction);
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            showSnackBarMessage(Const.STR_FAIL_UPDATE_DATA_TO_UI);
        }
    }

    private void setAllAirConditionData(AirCondition air) {
        binding.layoutLocationInfo.textDate.setText(air.getDataTimeTrim());
        setValuesAndImages(air.getKhaiGrade(), air.getKhaiValue(), Const.DRAWABLE_STATES_FACE, binding.layoutGeneral.imgKhai, binding.layoutGeneral.textValueGeneral, binding.layoutGeneral.textGramGeneral);

        setValuesAndImages(air.getPm10Grade1h(), air.getPm10Value(), Const.DRAWABLE_STATES, binding.layoutPmInfo.imgPm10, binding.layoutPmInfo.textValuePm10, binding.layoutPmInfo.textGramPm10);
        setValuesAndImages(air.getPm25Grade1h(), air.getPm25Value(), Const.DRAWABLE_STATES, binding.layoutPmInfo.imgPm25, binding.layoutPmInfo.textValuePm25, binding.layoutPmInfo.textGramPm25);

        setValuesAndImages(air.getO3Grade(), air.getO3Value(), Const.DRAWABLE_STATES, binding.layoutOthers.imgO3, binding.layoutOthers.textValueO3, null);
        setValuesAndImages(air.getNo2Grade(), air.getNo2Value(), Const.DRAWABLE_STATES, binding.layoutOthers.imgNo2, binding.layoutOthers.textValueNo2, null);

        setValuesAndImages(air.getCoGrade(), air.getCoValue(), Const.DRAWABLE_STATES, binding.layoutOthers.imgCo, binding.layoutOthers.textValueCo, null);
        setValuesAndImages(air.getSo2Grade(), air.getSo2Value(), Const.DRAWABLE_STATES, binding.layoutOthers.imgSo2, binding.layoutOthers.textValueSo2, null);
    }

    private void setValuesAndImages(String grade, String value, int[] imageType, ImageView img, TextView text, TextView gram) {
        Drawable resource;

        if (grade == null || grade.equals("")) {
            return;
        }
        if ( grade.equals("-")) {
            if (imageType == Const.DRAWABLE_STATES_FACE)
                mainView.setToolbarBackgroundColor(0);

            resource = ResourcesCompat.getDrawable(getResources(), imageType[0], null);
            text.setTextColor(Const.COLORS[0]);
            if(gram != null)
                gram.setTextColor(Const.COLORS[0]);
        }
        else {
            int GRADE = Integer.parseInt(grade);

            if (imageType == Const.DRAWABLE_STATES_FACE)
                mainView.setToolbarBackgroundColor(GRADE);

            resource = ResourcesCompat.getDrawable(getResources(), imageType[GRADE], null);
            text.setTextColor(Const.COLORS[GRADE]);
            if(gram != null)
                gram.setTextColor(Const.COLORS[GRADE]);
        }

        text.setText(value);
        img.setImageDrawable(resource);
    }

    private String getWidgetThemeAction(String widgetMode) {
        if (widgetMode.equals(Const.DARKWIDGET))
            return Const.WIDGET_DARK_RESPONSE_FROM_SERVER;
        else
            return Const.WIDGET_WHITE_RESPONSE_FROM_SERVER;
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String msg) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, 3000).show();
    }

    @Override
    public void checkGpsEnabled() {
        CheckConnectivity.checkGpsEnabled(getActivity());
    }

    @Override
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("위치정보 사용권한이 필요")
                            .setMessage("현재위치의 대기상태를 조회하기 위해 해당 권한이 필요합니다. 허용하시겠습니까?")
                            .setPositiveButton("네",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showToastMessage("권한 승인을 거부했습니다");
                                }
                            }).create().show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, MY_PERMISSION_REQUEST_LOCATION);
            }
            else {
                //항상 허용 체크한 경우
                return true;
            }
        }
        // ANDROID M 이하 버전
        else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION:
                //권한이 있는 경우
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToastMessage("위치정보 권한을 허용하셨습니다.");
                    isPermissionEnabled = true;
                }
                //권한이 없는 경우
                else {
                    showToastMessage("권한 요청을 거부했습니다.");
                }
                break;
        }
    }
}
