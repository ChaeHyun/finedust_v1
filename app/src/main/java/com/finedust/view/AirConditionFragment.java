package com.finedust.view;

import android.Manifest;
import android.content.DialogInterface;
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
import com.finedust.utils.SharedPreferences;


public class AirConditionFragment extends Fragment implements Views.AirConditionFragmentView {
    private static final String TAG = AirConditionFragment.class.getSimpleName();

    FragmentAirConditionBinding  binding;
    AirConditionFragmentPresenter airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());
    Views.MainActivityView mainView;
    SharedPreferences pref;

    final int MY_PERMISSION_REQUEST_LOCATION = 1000;
    boolean isPermissionEnabled = false;


    public AirConditionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_air_condition, container, false);
        binding.setAircondition(this);

        pref = new SharedPreferences(getActivity());

        airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());
        mainView = (MainActivity) getActivity();

        // launch at start, but not resume;
        String MODE = pref.getValue(SharedPreferences.CURRENT_MODE, Const.MODE[0]);
        Log.i(TAG+ " _checkCurrentMode ","   >> MODE : " + MODE);
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

            showToastMessage("최근 업데이트 시간 : " + recentData.getAirCondition().get(0).getDataTime()
                    + "\n주소 : " + recentData.getAddr().getAddr()
            + "\n측정소 : " + recentData.getSavedStations().get(0).getStationName());

            binding.layoutLocationInfo.textContentLocation.setText(recentData.getAddr().getAddr());
            setAllAirConditionData(air);
            binding.layoutStations.textContentWeathercenter.setText(weatherStation.toString());
        }
        catch (NullPointerException e) {
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
            if (Build.VERSION.SDK_INT >= 21 && imageType == Const.DRAWABLE_STATES_FACE) {
                getActivity().getWindow().setStatusBarColor(Const.TOOLBAR_COLORS_DARK[0]);
                mainView.setToolbarBackgroundColor(Const.TOOLBAR_COLORS[0]);
            }

            resource = ResourcesCompat.getDrawable(getResources(), imageType[0], null);
            text.setTextColor(Const.COLORS[0]);
            if(gram != null)
                gram.setTextColor(Const.COLORS[0]);
        }
        else {
            int GRADE = Integer.parseInt(grade);

            if (Build.VERSION.SDK_INT >= 21 && imageType == Const.DRAWABLE_STATES_FACE) {
                getActivity().getWindow().setStatusBarColor(Const.TOOLBAR_COLORS_DARK[GRADE]);
                mainView.setToolbarBackgroundColor(Const.TOOLBAR_COLORS[GRADE]);
            }

            resource = ResourcesCompat.getDrawable(getResources(), imageType[GRADE], null);
            text.setTextColor(Const.COLORS[GRADE]);
            if(gram != null)
                gram.setTextColor(Const.COLORS[GRADE]);
        }

        text.setText(value);
        img.setImageDrawable(resource);
    }


    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, 3000).show();
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
