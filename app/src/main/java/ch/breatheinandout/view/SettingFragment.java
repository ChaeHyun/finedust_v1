package ch.breatheinandout.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ch.breatheinandout.R;
import ch.breatheinandout.databinding.FragmentSettingBinding;
import ch.breatheinandout.model.Const;

import ch.breatheinandout.model.Addresses;
import ch.breatheinandout.presenter.MainActivityPresenter;
import ch.breatheinandout.presenter.SettingFragmentPresenter;
import ch.breatheinandout.utils.AppSharedPreferences;


public class SettingFragment extends Fragment implements Views.SettingFragmentView , View.OnClickListener {
    private final static String TAG = SettingFragment.class.getSimpleName();
    FragmentSettingBinding binding;
    AppSharedPreferences pref;

    Views.MainActivityView mainView;
    MainActivityPresenter mainActivityPresenter;

    SettingFragmentPresenter settingFragmentPresenter;

    EditText[] pm10_grade = new EditText[3];
    EditText[] pm25_grade = new EditText[3];
    EditText[] addresses_edit = new EditText[3];
    ImageView[] minusButton = new ImageView[3];

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        binding.setMinus(this);
        binding.setPlus(this);
        binding.setGrade(this);
        binding.setEtc(this);

        settingFragmentPresenter  = new SettingFragmentPresenter(this, getContext());
        pref = new AppSharedPreferences(getActivity());

        mainView = (MainActivity) getActivity();
        mainActivityPresenter = new MainActivityPresenter(mainView);
        setViewsForMemorizedAddresses();
        setViewsForGrade();

        setDeleteButtonVisibility(false);
        checkMemorizedAddresses();
        checkSelfGradeOn();

        return binding.getRoot();
    }

    private void setViewsForGrade() {
        pm10_grade[0] = binding.layoutChangeGrade.pm10EditBest;
        pm10_grade[1] = binding.layoutChangeGrade.pm10EditGood;
        pm10_grade[2] = binding.layoutChangeGrade.pm10EditBad;
        pm25_grade[0] = binding.layoutChangeGrade.pm25EditBest;
        pm25_grade[1] = binding.layoutChangeGrade.pm25EditGood;
        pm25_grade[2] = binding.layoutChangeGrade.pm25EditBad;
    }

    private void setViewsForMemorizedAddresses() {
        addresses_edit[0] = binding.layoutMemorizedAddress.editText1;
        addresses_edit[1] = binding.layoutMemorizedAddress.editText2;
        addresses_edit[2] = binding.layoutMemorizedAddress.editText3;

        minusButton[0] = binding.layoutMemorizedAddress.imgButtonMinus1;
        minusButton[1] = binding.layoutMemorizedAddress.imgButtonMinus2;
        minusButton[2] = binding.layoutMemorizedAddress.imgButtonMinus3;

    }

    private void checkMemorizedAddresses() {
        for(int i = 1; i < 4; i++) {
            Addresses save = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[i], Const.EMPTY_STRING, new Addresses());
            if (save != null) {
                setDeleteButtonVisibility(true, i, save.getAddr());
            }
        }
    }

    private void checkSelfGradeOn() {
        if (pref.getValue(AppSharedPreferences.GRADE_MODE, Const.ON_OFF[1]).equals(Const.ON_OFF[0])) {
            binding.layoutChangeGrade.switchGrade.setChecked(true);
            for( int i=0; i < 3; i++) {
                pm10_grade[i].setText(pref.getValue(Const.SELF_GRADE_PM10[i], ""));
                pm25_grade[i].setText(pref.getValue(Const.SELF_GRADE_PM25[i], ""));
            }
        }
    }

    private void setDeleteButtonVisibility(boolean on) {
        if (!on) {
            for (int i = 0; i < 3; i++) {
                addresses_edit[i].setText(Const.EMPTY_STRING);
                minusButton[i].setEnabled(false);
                minusButton[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void  setDeleteButtonVisibility(boolean on, int pos, String address) {
        if (on) {
            minusButton[pos-1].setEnabled(true);
            minusButton[pos-1].setVisibility(View.VISIBLE);
            addresses_edit[pos-1].setText(address);
            addresses_edit[pos-1].setOnClickListener(this);
        }
        else {
            minusButton[pos-1].setEnabled(false);
            minusButton[pos-1].setVisibility(View.INVISIBLE);
            addresses_edit[pos-1].setText(Const.EMPTY_STRING);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            mainView.saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            mainView.saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 3) {
            mainView.saveAddrInPreferences(requestCode, data, AppSharedPreferences.MEMORIZED_LOCATIONS[requestCode]);
            setDeleteButtonVisibility(true, requestCode, data.getStringExtra("Addr"));
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            pref.put(AppSharedPreferences.GRADE_MODE, data.getStringExtra(AppSharedPreferences.GRADE_MODE));
            for (int i = 0; i < 3; i++) {
                pref.put(Const.SELF_GRADE_PM10[i], data.getStringExtra(Const.SELF_GRADE_PM10[i]));
                pref.put(Const.SELF_GRADE_PM25[i], data.getStringExtra(Const.SELF_GRADE_PM25[i]));
                pm10_grade[i].setText(data.getStringExtra(Const.SELF_GRADE_PM10[i]));
                pm25_grade[i].setText(data.getStringExtra(Const.SELF_GRADE_PM25[i]));
            }

            binding.layoutChangeGrade.switchGrade.setChecked(true);
        }
        else if(resultCode == Activity.RESULT_CANCELED && requestCode == 5) {
            pref.put(AppSharedPreferences.GRADE_MODE, Const.ON_OFF[1]);
            binding.layoutChangeGrade.switchGrade.setChecked(false);
        }
    }


    public void onPlusButtonClick(View view) {
        Intent intent = new Intent(getContext(), SearchAddressActivity.class);

        if (binding.layoutMemorizedAddress.editText1.getText().toString().equals("")) {
            startActivityForResult(intent, 1);
        }
        else if (binding.layoutMemorizedAddress.editText2.getText().toString().equals("")) {
            startActivityForResult(intent, 2);
        }
        else if (binding.layoutMemorizedAddress.editText3.getText().toString().equals("")) {
            startActivityForResult(intent, 3);
        }
        else
            Toast.makeText(getContext(), "더 이상 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
    }

    public void onMinusButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_button_minus1:
                pref.removeValue(AppSharedPreferences.MEMORIZED_LOCATIONS[1]);
                pref.removeValue(AppSharedPreferences.RECENT_DATA[1]);
                pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[0]);
                setDeleteButtonVisibility(false, 1, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 1, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
            case R.id.img_button_minus2:
                pref.removeValue(AppSharedPreferences.MEMORIZED_LOCATIONS[2]);
                pref.removeValue(AppSharedPreferences.RECENT_DATA[2]);
                pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[0]);
                setDeleteButtonVisibility(false, 2, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 2, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
            case R.id.img_button_minus3:
                pref.removeValue(AppSharedPreferences.MEMORIZED_LOCATIONS[3]);
                pref.removeValue(AppSharedPreferences.RECENT_DATA[3]);
                pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[0]);
                setDeleteButtonVisibility(false, 3, Const.EMPTY_STRING);
                mainView.setNavigationTitle(Const.EMPTY_STRING, 3, Const.NAVI_ICON_LOCATION_NOT_SAVED);
                break;
        }
    }

    public void onEtcLayoutButtonsClick(View view) {
        switch (view.getId()) {
            case R.id.layout_mail:
                Uri uri = Uri.parse("mailto:zephyrish9@gmail.com");
                Intent email = new Intent(Intent.ACTION_SENDTO, uri);
                email.putExtra(Intent.EXTRA_SUBJECT, "들숨날숨 앱 문의사항");
                email.putExtra(Intent.EXTRA_TEXT, "문의사항을 입력해주세요.");
                startActivity(email);

                break;
            case R.id.layout_like:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=ch.breatheinandout");
                shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "[들숨날숨 - 대기환경/미세먼지/초미세먼지]\n");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "\n우리동네 실시간 대기환경 쉽게 확인하세요." + "\n" + marketUri.toString());
                startActivity(Intent.createChooser(shareIntent, "들숨날숨 추천하기"));

                break;
        }
    }

    public void onGradeSwitchClick(View view) {
        if (binding.layoutChangeGrade.switchGrade.isChecked()) {
            Intent intent = new Intent(getContext(), ChangeGradeActivity.class);
            startActivityForResult(intent, 5);
        }
        else {
            pref.put(AppSharedPreferences.GRADE_MODE, Const.ON_OFF[1]);
            for (int i = 0; i < 3; i++) {
                pm10_grade[i].setText(Const.EMPTY_STRING);
                pm25_grade[i].setText(Const.EMPTY_STRING);
            }
        }
        settingFragmentPresenter.deleteSavedData();
    }

    @Override
    public void onClick(View view) {
        String location = "";
        switch (view.getId()) {
            case R.id.editText1:
                location = binding.layoutMemorizedAddress.editText1.getText().toString();
                if (!location.equals("")) {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[1]);
                    mainView.setNavigationChecked(1, true);
                }
                break;
            case R.id.editText2:
                location = binding.layoutMemorizedAddress.editText2.getText().toString();
                if (!location.equals("")) {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[2]);
                    mainView.setNavigationChecked(2, true);
                }
                break;
            case R.id.editText3:
                location = binding.layoutMemorizedAddress.editText3.getText().toString();
                if (!location.equals("")) {
                    pref.put(AppSharedPreferences.CURRENT_MODE, Const.MODE[3]);
                    mainView.setNavigationChecked(3, true);
                }
                break;
        }

        if (!location.equals("")) {
            Toast.makeText(getActivity(), "지역설정 : " + location , Toast.LENGTH_SHORT).show();
            mainView.fragmentReplace(new AirConditionFragment());
        }
    }


}
