package com.finedust.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.FragmentSettingBinding;
import com.finedust.model.Const;
import com.finedust.model.pref.MemorizedAddress;
import com.finedust.presenter.MainActivityPresenter;
import com.finedust.utils.SharedPreferences;

public class SettingFragment extends Fragment implements Views.SettingFragmentView {
    private final static String TAG = SettingFragment.class.getSimpleName();
    FragmentSettingBinding binding;
    SharedPreferences pref;

    Views.MainActivityView mainView;
    MainActivityPresenter mainActivityPresenter;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        binding.setMinus(this);
        binding.setPlus(this);

        pref = new SharedPreferences(getActivity());
        mainView = (MainActivity) getActivity();
        mainActivityPresenter = new MainActivityPresenter(mainView);

        setDeleteButtonVisibility(false);
        checkMemorizedAddresses();

        return binding.getRoot();
    }

    private void checkMemorizedAddresses() {
        for(int i = 0; i < 3; i++) {
            MemorizedAddress save = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[i], "", new MemorizedAddress());
            if (save != null) {
                setDeleteButtonVisibility(true, i, save.getMemorizedAddress());
            }
        }
    }

    private void setDeleteButtonVisibility(boolean on) {
        if (!on) {
            binding.layoutMemorizedAddress.editText1.setText("");
            binding.layoutMemorizedAddress.imgButtonMinus1.setEnabled(false);
            binding.layoutMemorizedAddress.imgButtonMinus1.setVisibility(View.INVISIBLE);

            binding.layoutMemorizedAddress.editText2.setText("");
            binding.layoutMemorizedAddress.imgButtonMinus2.setEnabled(false);
            binding.layoutMemorizedAddress.imgButtonMinus2.setVisibility(View.INVISIBLE);

            binding.layoutMemorizedAddress.editText3.setText("");
            binding.layoutMemorizedAddress.imgButtonMinus3.setEnabled(false);
            binding.layoutMemorizedAddress.imgButtonMinus3.setVisibility(View.INVISIBLE);
            }
    }

    private void  setDeleteButtonVisibility(boolean on, int pos, String address) {
        if (on) {
            switch (pos) {
                case 0:
                    binding.layoutMemorizedAddress.imgButtonMinus1.setEnabled(true);
                    binding.layoutMemorizedAddress.imgButtonMinus1.setVisibility(View.VISIBLE);

                    binding.layoutMemorizedAddress.editText1.setText(address);
                    break;
                case 1:
                    binding.layoutMemorizedAddress.imgButtonMinus2.setEnabled(true);
                    binding.layoutMemorizedAddress.imgButtonMinus2.setVisibility(View.VISIBLE);

                    binding.layoutMemorizedAddress.editText2.setText(address);
                    break;
                case 2:
                    binding.layoutMemorizedAddress.imgButtonMinus3.setEnabled(true);
                    binding.layoutMemorizedAddress.imgButtonMinus3.setVisibility(View.VISIBLE);

                    binding.layoutMemorizedAddress.editText3.setText(address);
                    break;
            }
        }
        else {
            switch (pos) {
                case 0:
                    binding.layoutMemorizedAddress.editText1.setText("");
                    binding.layoutMemorizedAddress.imgButtonMinus1.setEnabled(false);
                    binding.layoutMemorizedAddress.imgButtonMinus1.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    binding.layoutMemorizedAddress.editText2.setText("");
                    binding.layoutMemorizedAddress.imgButtonMinus2.setEnabled(false);
                    binding.layoutMemorizedAddress.imgButtonMinus2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    binding.layoutMemorizedAddress.editText3.setText("");
                    binding.layoutMemorizedAddress.imgButtonMinus3.setEnabled(false);
                    binding.layoutMemorizedAddress.imgButtonMinus3.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            mainView.saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));

            //Preferences 확인
            MemorizedAddress addr = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[requestCode], "", new MemorizedAddress());
            Log.i(TAG, " > Pref 확인 : " + ((MemorizedAddress)pref.getObject("MemorizedAddress_One", "", new MemorizedAddress())).getMemorizedAddress());

        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            mainView.saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));

            //Preferences 확인
            MemorizedAddress addr = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[requestCode], "", new MemorizedAddress());
            Log.i(TAG, " > Pref 확인 : " + ((MemorizedAddress)pref.getObject("MemorizedAddress_One", "", new MemorizedAddress())).getMemorizedAddress());


        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            mainView.saveAddrInPreferences(requestCode, data, Const.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));

            //Preferences 확인
            MemorizedAddress addr = (MemorizedAddress) pref.getObject(Const.MEMORIZED_LOCATIONS[requestCode], "", new MemorizedAddress());
            Log.i(TAG, " > Pref 확인 : " + addr.getMemorizedAddress());

        }
    }


    public void onPlusButtonClick(View view) {
        Intent intent = new Intent(getContext(), SearchAddressActivity.class);

        if (binding.layoutMemorizedAddress.editText1.getText().toString().equals("")) {
            startActivityForResult(intent, 0);
        }
        else if (binding.layoutMemorizedAddress.editText2.getText().toString().equals("")) {
            startActivityForResult(intent, 1);
        }
        else if (binding.layoutMemorizedAddress.editText3.getText().toString().equals("")) {
            startActivityForResult(intent, 2);
        }
        else
            Toast.makeText(getContext(), "더 이상 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
    }


    public void onMinusButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_button_minus1:
                pref.removeValue(Const.MEMORIZED_LOCATIONS[0]);
                setDeleteButtonVisibility(false, 0, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 0, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
            case R.id.img_button_minus2:
                pref.removeValue(Const.MEMORIZED_LOCATIONS[1]);
                setDeleteButtonVisibility(false, 1, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 1, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
            case R.id.img_button_minus3:
                pref.removeValue(Const.MEMORIZED_LOCATIONS[2]);
                setDeleteButtonVisibility(false, 2, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 2, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText1:
                break;
            case R.id.editText2:
                break;
            case R.id.editText3:
                break;
        }

    }


}
