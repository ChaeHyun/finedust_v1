package com.finedust.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.WidgetDarkConfigureBinding;
import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.utils.SharedPreferences;


public class WidgetDarkConfigureActivity extends AppCompatActivity {
    private static final String TAG = WidgetDarkConfigureActivity.class.getSimpleName();

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    SharedPreferences pref;
    WidgetDarkConfigureBinding binding;

    String[] locationStrings = new String[4];
    RadioButton[] locationCheck = new RadioButton[4];
    Addresses[] saveLocations = new Addresses[4];


    public WidgetDarkConfigureActivity() {
        super();
    }

    /*

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    */

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        binding = DataBindingUtil.setContentView(this, R.layout.widget_dark_configure);

        pref = new SharedPreferences(this);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }



        getPreviousSettingValues(mAppWidgetId);

        //mAppWidgetText.setText(loadTitlePref(WidgetDarkConfigureActivity.this, mAppWidgetId));
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
        String mode = pref.getValue(SharedPreferences.WIDGET_MODE + mAppWidgetId, Const.MODE[0]);
        if (mode.equals(Const.MODE[1]))
            binding.layoutSetLocation.locOne.setChecked(true);

        else if (mode.equals(Const.MODE[2]))
            binding.layoutSetLocation.locTwo.setChecked(true);

        else if (mode.equals(Const.MODE[3]))
            binding.layoutSetLocation.locThree.setChecked(true);


        for (int i = 1; i < locationCheck.length; i++) {
            try {
                saveLocations[i] = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[i], Const.EMPTY_STRING, new Addresses());
                locationStrings[i] = saveLocations[i].getAddr();
                if (!locationStrings[i].equals(Const.EMPTY_STRING)) {
                    locationCheck[i].setText(locationStrings[i]);
                    locationCheck[i].setClickable(true);
                }
            }
            catch (NullPointerException e) {
                //e.printStackTrace();
            }
        }


        // progressbar값 불러오기
        String intervalValue = pref.getValue( SharedPreferences.INTERVAL + mAppWidgetId , Const.WIDGET_DEFAULT_INTERVAL);
        String transparentValue = pref.getValue( SharedPreferences.TRANSPARENT + mAppWidgetId , Const.WIDGET_DEFAULT_TRANSPARENT);

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
                selectedLocation  = locationCheck[i].getText().toString();
                Log.i(TAG, "SELECTEDD LOCOATION : " + selectedLocation);
                checkRadioButtons = true;
                break;
            }
        }

        if (!checkRadioButtons) {
            Toast.makeText(this, "저장 위치를 등록 후 사용하세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String interval = String.valueOf(binding.layoutUpdate.seekBarUpdate.getProgress());
        String transparent = String.valueOf(binding.layoutUpdate.seekBarTransparent.getProgress());


        // Save setting values to SharedPreferences.
        pref.put(SharedPreferences.INTERVAL + mAppWidgetId , interval);
        pref.put(SharedPreferences.TRANSPARENT + mAppWidgetId , transparent);
        pref.put(SharedPreferences.WIDGET_SELECTED_LOCATION_INDEX + mAppWidgetId , String.valueOf(selectedRadioButton));
        pref.put(SharedPreferences.WIDGET_MODE + mAppWidgetId , Const.MODE[selectedRadioButton]);

        Log.i(TAG, "# [ Widget Configuration Check ]  , Interval : " + interval + " , MODE : " + Const.MODE[selectedRadioButton] + " , trans : " + transparent + " , widgetId : " + mAppWidgetId);

        // Broadcast - send a update flag.
        Intent update = new Intent(this, WidgetDark.class);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/") , String.valueOf(mAppWidgetId)));
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        update.putExtra("widgetId" , String.valueOf(mAppWidgetId));
        update.putExtra("transparent" , transparent);
        update.putExtra("location" , selectedLocation);
        sendBroadcast(update);

    }

    static void deletePrefForWidgets(Context context, int appWidgetId) {
        SharedPreferences prefs = new SharedPreferences(context);
        prefs.removeValue(SharedPreferences.INTERVAL + appWidgetId);
        prefs.removeValue(SharedPreferences.TRANSPARENT + appWidgetId);
        prefs.removeValue(SharedPreferences.WIDGET_SELECTED_LOCATION_INDEX + appWidgetId);
        prefs.removeValue(SharedPreferences.WIDGET_MODE + appWidgetId);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case  R.id.button_save:
                    final Context context = WidgetDarkConfigureActivity.this;
                    // It is the responsibility of the configuration activity to update the app widget
                    //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    //WidgetDark.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                    updateWidget();

                    // Make sure we pass back the original appWidgetId
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();

                    break;
                case R.id.button_cancel:
                    resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
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

