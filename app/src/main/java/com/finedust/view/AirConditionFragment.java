package com.finedust.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.finedust.model.GpsData;
import com.finedust.model.adapter.MyAdapter;
import com.finedust.presenter.AirConditionFragmentPresenter;
import com.finedust.utils.CheckConnectivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AirConditionFragment extends Fragment implements Views.AirConditionFragmentView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = AirConditionFragment.class.getSimpleName();

    FragmentAirConditionBinding  binding;
    AirConditionFragmentPresenter airConditionFragmentPresenter = new AirConditionFragmentPresenter(this);
    private MyAdapter adapter;

    GoogleApiClient googleApiClient;
    Location lastLocation;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

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


        getGpsCoordinates();

        return binding.getRoot();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onSampleButtonClick(View view) {
        String temporaryStationName = "호림동";
        Log.i(TAG, "onSamplelButtonoClick()");
        binding.button.setText("Changed");

        //Presenter를 이용해서 airConditionPresenter의 onSampleButtonClicked() 메소드를 호출.
        //프리젠터에 호출을 요청하고 난 뒤에 역할 끝.

        // .onSampleButtonClicked() 내부에서 Business Logic을 처리한 후 View에게 업데이트를 요청. (view.showTestToastMessage)
        airConditionFragmentPresenter.getAirConditionData(getContext(), temporaryStationName);
        airConditionFragmentPresenter.onSampleButtonClicked();

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
    public void getGpsCoordinates() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG,"Location Services Connected");

        isPermissionEnabled = checkPermission();
        CheckConnectivity.checkGpsEnabled(getActivity());

        if(isPermissionEnabled)
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);

            if(lastLocation != null) {
                handleNewLocation(lastLocation);
            }
            else {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    binding.textView.setText("현재 위치를 찾을 수 없습니다.");
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.i(TAG, "GoogleApiClient is NOT Connected yet");
                }
            }
        }
        else {
            showToastMessage("권한 허가가 필요합니다.");
        }


    }


    private void handleNewLocation(Location location) {
        Log.i(TAG, location.toString());
        if(isAdded())
        {
            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.KOREA);

            GpsData CoordData = new GpsData();
            CoordData.setWgs_x(String.valueOf(lastLocation.getLongitude()));
            CoordData.setWgs_y(String.valueOf(lastLocation.getLatitude()));

            Log.i(TAG, " # Check Wgs Coord : " + CoordData.getWgs_x() + " , " + CoordData.getWgs_y());

            try {
                List<Address> addresses = gcd.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
                String msg = addresses.get(0).getLocality() + " " + addresses.get(0).getSubLocality() + " " + addresses.get(0).getThoroughfare();
                showToastMessage(msg);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // 좌표변환
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i(TAG, "Location services connection failed with code -> " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private boolean checkPermission() {
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
            else{
                //항상 허용 체크한 경우
                return true;
            }
        }
        // ANDROID M 이하 버전
        else {
            //mDialog = new PermissionDialog(this);
            //mDialog.PermissionCheckAll();
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
