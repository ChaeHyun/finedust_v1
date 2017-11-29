package com.finedust.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.WidgetConfigureBinding;
import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.utils.AppSharedPreferences;
import com.finedust.utils.ChangeFont;

public class WidgetDarkConfigureActivity extends AppCompatActivity {
    private static final String TAG = WidgetDarkConfigureActivity.class.getSimpleName();

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    AppSharedPreferences pref;
    WidgetConfigureBinding binding;

    String[] locationStrings = new String[4];
    RadioButton[] locationCheck = new RadioButton[4];
    Addresses[] saveLocations = new Addresses[4];

    Intent resultValue = new Intent();

    public WidgetDarkConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChangeFont.Typekit(this, Const.FONT_DEFAULT_NORMAL);

        binding = DataBindingUtil.setContentView(this, R.layout.widget_configure);

        pref = new AppSharedPreferences(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        getPreviousSettingValues(mAppWidgetId);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChangeFont.TypekitContextWrapper(newBase));
    }

    private void getPreviousSettingValues(final int mAppWidgetId) {
        locationCheck[0] = binding.layoutSetLocation.locZero;
        locationCheck[1] = binding.layoutSetLocation.locOne;
        locationCheck[2] = binding.layoutSetLocation.locTwo;
        locationCheck[3] = binding.layoutSetLocation.locThree;

        binding.buttonSave.setOnClickListener(mOnClickListener);
        binding.buttonCancel.setOnClickListener(mOnClickListener);

        for (int i = 0; i < saveLocations.length; i++) {
            saveLocations[i] = new Addresses();
        }

        // 저장된 주소 불러오기
        String mode = pref.getValue(AppSharedPreferences.WIDGET_MODE + mAppWidgetId, Const.MODE[0]);
        if (mode.equals(Const.MODE[1]))
            binding.layoutSetLocation.locOne.setChecked(true);

        else if (mode.equals(Const.MODE[2]))
            binding.layoutSetLocation.locTwo.setChecked(true);

        else if (mode.equals(Const.MODE[3]))
            binding.layoutSetLocation.locThree.setChecked(true);


        for (int i = 1; i < locationCheck.length; i++) {
            try {
                saveLocations[i] = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[i], Const.EMPTY_STRING, new Addresses());
                locationStrings[i] = saveLocations[i].getAddr();
                if (!locationStrings[i].equals(Const.EMPTY_STRING)) {
                    locationCheck[i].setText(locationStrings[i]);
                    locationCheck[i].setClickable(true);
                }
            }
            catch (NullPointerException e) {
                //e.printStackTrace();
                // 실제 저장된 주소가 삭제 되었을 경우 선택할 수 없도록 변경.
                locationCheck[i].setText("-");
                locationCheck[i].setClickable(false);
                locationCheck[i].setChecked(false);
            }
        }

        // progressbar값 불러오기
        String intervalValue = pref.getValue( AppSharedPreferences.INTERVAL + mAppWidgetId , Const.WIDGET_DEFAULT_INTERVAL);
        String transparentValue = pref.getValue( AppSharedPreferences.TRANSPARENT + mAppWidgetId , Const.WIDGET_DEFAULT_TRANSPARENT);

        binding.layoutUpdate.valueUpdate.setText("업데이트 주기 : " + intervalValue + " 시간");
        binding.layoutUpdate.valueTransparent.setText("투명도 : " + transparentValue + "");
        binding.layoutUpdate.seekBarUpdate.setProgress(Integer.parseInt(intervalValue));
        binding.layoutUpdate.seekBarTransparent.setProgress(Integer.parseInt(transparentValue));
        binding.layoutUpdate.seekBarUpdate.setOnSeekBarChangeListener(updatePeriod_seekBarListener);
        binding.layoutUpdate.seekBarTransparent.setOnSeekBarChangeListener(transpaprent_seekBarListener);
    }

    public void updateWidget() {
        boolean checkRadioButtons = false;
        int selectedRadioButton = 0;
        String selectedLocation = null;

        for (int i = 1; i < locationCheck.length; i++) {
            if (locationCheck[i].isChecked()) {
                selectedRadioButton = i;
                selectedLocation = locationCheck[i].getText().toString();
                Log.i(TAG, "SELECTED LOCATION : " + selectedLocation);
                checkRadioButtons = true;
                break;
            }
        }

        if (!checkRadioButtons || selectedRadioButton == 0) {
            Toast.makeText(this, "저장 위치를 등록 후 사용하세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String interval = String.valueOf(binding.layoutUpdate.seekBarUpdate.getProgress());
        String transparent = String.valueOf(binding.layoutUpdate.seekBarTransparent.getProgress());

        // Save setting values to SharedPreferences.
        pref.put(AppSharedPreferences.INTERVAL + mAppWidgetId , interval);
        pref.put(AppSharedPreferences.TRANSPARENT + mAppWidgetId , transparent);
        pref.put(AppSharedPreferences.WIDGET_MODE + mAppWidgetId , Const.MODE[selectedRadioButton]);

        try {
            Addresses savedLocation = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[selectedRadioButton], Const.EMPTY_STRING, new Addresses());
            pref.put(AppSharedPreferences.WIDGET_LOCATION + mAppWidgetId, savedLocation.getAddr());
            Log.i(TAG, "# [ Widget Configuration Check ]  , Interval : " + interval + " , MODE : " + Const.MODE[selectedRadioButton] + " , trans : " + transparent + " , widgetId : " + mAppWidgetId + "\n   location : " + savedLocation.getAddr());
        }
        catch (NullPointerException e) {
            Toast.makeText(this, "등록되지 않은 저장위치 입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Broadcast - send a update flag.
        Intent update = new Intent(this, WidgetDark.class);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.setData(Uri.withAppendedPath(Uri.parse(Const.DARKWIDGET + "://widget/id/") , String.valueOf(mAppWidgetId)));
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        sendBroadcast(update);
    }

    static void deletePrefForWidgets(Context context, int appWidgetId) {
        AppSharedPreferences prefs = new AppSharedPreferences(context);
        prefs.removeValue(AppSharedPreferences.INTERVAL + appWidgetId);
        prefs.removeValue(AppSharedPreferences.TRANSPARENT + appWidgetId);
        prefs.removeValue(AppSharedPreferences.WIDGET_SELECTED_LOCATION_INDEX + appWidgetId);
        prefs.removeValue(AppSharedPreferences.WIDGET_MODE + appWidgetId);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case  R.id.button_save:
                    // restrict the number of widget.
                    int[] appWidgetIds = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetDark.class));

                    if (appWidgetIds.length > 3) {
                        Toast.makeText(view.getContext(), "위젯은 총 3개까지 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED, resultValue);
                        finish();
                    }
                    else {
                        updateWidget();

                        setResult(RESULT_OK, resultValue);
                        finish();
                    }

                    break;
                case R.id.button_cancel:
                    setResult(RESULT_CANCELED, resultValue);
                    finish();
            }
        }
    };

    SeekBar.OnSeekBarChangeListener updatePeriod_seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
                seekBar.setProgress(progress);
            }
            binding.layoutUpdate.valueUpdate.setText("업데이트 주기 : " + progress + " 시간");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    SeekBar.OnSeekBarChangeListener transpaprent_seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            binding.layoutUpdate.valueTransparent.setText("투명도 : " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
