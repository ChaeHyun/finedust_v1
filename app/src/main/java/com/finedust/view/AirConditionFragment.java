package com.finedust.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.FragmentAirConditionBinding;
import com.finedust.model.AirCondition;

import com.finedust.model.adapter.MyAdapter;
import com.finedust.presenter.AirConditionFragmentPresenter;
import com.finedust.utils.CheckConnectivity;

import java.util.ArrayList;


public class AirConditionFragment extends Fragment implements Views.AirConditionFragmentView {
    private static final String TAG = AirConditionFragment.class.getSimpleName();

    FragmentAirConditionBinding  binding;
    AirConditionFragmentPresenter airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());
    private MyAdapter adapter;

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

        binding.button.setText("버튼");
        binding.listView.setOnItemClickListener(onClickListViewItem);

        airConditionFragmentPresenter = new AirConditionFragmentPresenter(this, getContext());

        return binding.getRoot();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(TAG, "onResume()");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        airConditionFragmentPresenter.onPause();
    }

    private AdapterView.OnItemClickListener onClickListViewItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if(adapter != null) {
                Toast.makeText(getContext(),
                        "PM10 Value : " + adapter.getItem(position).getPm10Value()
                                + "\nCO Value : " + adapter.getItem(position).getCoValue()
                                + "\nSO2 Value : " + adapter.getItem(position).getSo2Value()
                        , Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onSampleButtonClick(View view) {
        Log.i(TAG, "onSamplelButtonoClick()");
        binding.button.setText("Changed");

        //Presenter를 이용해서 airConditionPresenter의 onSampleButtonClicked() 메소드를 호출.
        //프리젠터에 호출을 요청하고 난 뒤에 역할 끝.
        airConditionFragmentPresenter.getGPSCoordinates();

    }

    @Override
    public void updateAirConditionData(ArrayList<AirCondition> data) {
        Log.i(TAG, "updating Air Condition Data to List Adapter");
        adapter = new MyAdapter(getContext(), 0, data);
        binding.listView.setAdapter(adapter);
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
