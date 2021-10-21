package ch.breatheinandout.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ch.breatheinandout.R;
import ch.breatheinandout.databinding.SettingChangeGradeBinding;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.utils.AppSharedPreferences;
import ch.breatheinandout.utils.ChangeFont;


public class ChangeGradeActivity extends AppCompatActivity implements Views.ChangeGradeActivityView , View.OnClickListener {
    private static final String TAG = ChangeGradeActivity.class.getSimpleName();

    SettingChangeGradeBinding binding;
    AppSharedPreferences pref;
    
    int[] progressValuePm10 = new int[3];
    int[] progressValuePm25 = new int[3];
    TextView[] textViews_Pm10 = new TextView[3];
    SeekBar[] seekBars_Pm10 = new SeekBar[3];
    TextView[] textViews_Pm25 = new TextView[3];
    SeekBar[] seekBars_Pm25 = new SeekBar[3];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChangeFont.Typekit(this, Const.FONT_DEFAULT_NORMAL, Const.FONT_DEFAULT_BOLD);

        pref = new AppSharedPreferences(this);
        binding = DataBindingUtil.setContentView(this, R.layout.setting_change_grade);
        binding.setButton(this);

        bindingViews();
        setActionBarTitle("구간설정");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChangeFont.TypekitContextWrapper(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle(String title) {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void bindingViews() {
        seekBars_Pm10[0] = binding.layoutGradePm10.pm10SeekBarBest;
        seekBars_Pm10[1] = binding.layoutGradePm10.pm10SeekBarGood;
        seekBars_Pm10[2] = binding.layoutGradePm10.pm10SeekBarBad;

        textViews_Pm10[0] = binding.layoutGradePm10.pm10TextViewBest;
        textViews_Pm10[1] = binding.layoutGradePm10.pm10TextViewGood;
        textViews_Pm10[2] = binding.layoutGradePm10.pm10TextViewBad;

        seekBars_Pm25[0] = binding.layoutGradePm25.pm25SeekBarBest;
        seekBars_Pm25[1] = binding.layoutGradePm25.pm25SeekBarGood;
        seekBars_Pm25[2] = binding.layoutGradePm25.pm25SeekBarBad;

        textViews_Pm25[0] = binding.layoutGradePm25.pm25TextViewBest;
        textViews_Pm25[1] = binding.layoutGradePm25.pm25TextViewGood;
        textViews_Pm25[2] = binding.layoutGradePm25.pm25TextViewBad;

        binding.layoutGradePm10.pm10ButtonAir.setOnClickListener(this);
        binding.layoutGradePm10.pm10ButtonWho.setOnClickListener(this);
        binding.layoutGradePm25.pm25ButtonAir.setOnClickListener(this);
        binding.layoutGradePm25.pm25ButtonWho.setOnClickListener(this);

        seekBars_Pm10[0].setOnSeekBarChangeListener(PM10_SeekBar_Listener_Best);
        seekBars_Pm10[1].setOnSeekBarChangeListener(PM10_SeekBar_Listener_Good);
        seekBars_Pm10[2].setOnSeekBarChangeListener(PM10_SeekBar_Listener_Bad);
        seekBars_Pm25[0].setOnSeekBarChangeListener(PM25_SeekBar_Listener_Best);
        seekBars_Pm25[1].setOnSeekBarChangeListener(PM25_SeekBar_Listener_Good);
        seekBars_Pm25[2].setOnSeekBarChangeListener(PM25_SeekBar_Listener_Bad);
    } 

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pm10_button_air:
                setDefaultGrade(seekBars_Pm10, textViews_Pm10, Const.STANDARD_FOR_PM10_AIRKOREA);
                break;
            case R.id.pm10_button_who:
                setDefaultGrade(seekBars_Pm10, textViews_Pm10, Const.STANDARD_FOR_PM10_WHO);
                break;
            case R.id.pm25_button_air:
                setDefaultGrade(seekBars_Pm25, textViews_Pm25, Const.STANDARD_FOR_PM25_AIRKOREA);
                break;
            case R.id.pm25_button_who:
                setDefaultGrade(seekBars_Pm25, textViews_Pm25, Const.STANDARD_FOR_PM25_WHO);
                break;
        }
    }
    
    private void setDefaultGrade(SeekBar[] seekBar, TextView[] textView, int[] value) {
        for (int i = 0; i < value.length; i++) {
            if (i == 0) {
                seekBar[i].setProgress(value[i]);
                textView[i].setText(String.valueOf(value[i]));
            }
            else {
                seekBar[i].setProgress(value[i] - value[i-1]);
                textView[i].setText(String.valueOf(value[i]));
            }
        }
    }

    public void onSaveButtonClick(View v) {
        Log.i(TAG, "onClickSaveButton()");

        if (textViews_Pm10[0].getText().toString().equals("") || textViews_Pm25[0].getText().toString().equals(""))
            Snackbar.make(binding.getRoot(), "구간설정이 올바르지 않습니다. 다시 시도하세요.", 3000).show();

        else {
            Intent result = new Intent();
            result.putExtra(AppSharedPreferences.GRADE_MODE , Const.ON_OFF[0]);
            for (int i=0; i < Const.SELF_GRADE_PM10.length; i++) {
                result.putExtra(Const.SELF_GRADE_PM10[i], textViews_Pm10[i].getText().toString());
                result.putExtra(Const.SELF_GRADE_PM25[i], textViews_Pm25[i].getText().toString());
            }

            setResult(RESULT_OK, result);
            finish();
        }
    }

    public void onCancelButtonClick(View v) {
        Log.i(TAG, "onClickCandelButton()");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    SeekBar.OnSeekBarChangeListener PM10_SeekBar_Listener_Best = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);

            binding.layoutGradePm10.pm10TextViewBest.setText(String.valueOf(progress));
            binding.layoutGradePm10.pm10TextViewGood.setText(String.valueOf(progress+1));
            binding.layoutGradePm10.pm10TextViewBad.setText(String.valueOf(progress+2));
            binding.layoutGradePm10.pm10SeekBarGood.setProgress(0);
            binding.layoutGradePm10.pm10SeekBarBad.setProgress(0);
            progressValuePm10[0] = progress;
            progressValuePm10[1] = progress+1;
            progressValuePm10[2] = progress+2;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    SeekBar.OnSeekBarChangeListener PM10_SeekBar_Listener_Good = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);
            progress += progressValuePm10[0];

            binding.layoutGradePm10.pm10TextViewGood.setText(String.valueOf(progress));
            binding.layoutGradePm10.pm10TextViewBad.setText(String.valueOf(progress+1));
            binding.layoutGradePm10.pm10SeekBarBad.setProgress(0);
            progressValuePm10[1] = progress;
            progressValuePm10[2] = progress+1;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    SeekBar.OnSeekBarChangeListener PM10_SeekBar_Listener_Bad = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);
            progress += progressValuePm10[1];
            binding.layoutGradePm10.pm10TextViewBad.setText(String.valueOf(progress));
            progressValuePm10[2] = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    
    SeekBar.OnSeekBarChangeListener PM25_SeekBar_Listener_Best = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if(progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);

            binding.layoutGradePm25.pm25TextViewBest.setText(String.valueOf(progress));
            binding.layoutGradePm25.pm25TextViewGood.setText(String.valueOf(progress+1));
            binding.layoutGradePm25.pm25TextViewBad.setText(String.valueOf(progress+2));
            binding.layoutGradePm25.pm25SeekBarGood.setProgress(0);
            binding.layoutGradePm25.pm25SeekBarBad.setProgress(0);
            progressValuePm25[0] = progress;
            progressValuePm25[1] = progress+1;
            progressValuePm25[2] = progress+2;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    SeekBar.OnSeekBarChangeListener PM25_SeekBar_Listener_Good = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);
            progress += progressValuePm25[0];

            binding.layoutGradePm25.pm25TextViewGood.setText(String.valueOf(progress));
            binding.layoutGradePm25.pm25TextViewBad.setText(String.valueOf(progress+1));
            binding.layoutGradePm25.pm25SeekBarBad.setProgress(0);
            progressValuePm25[1] = progress;
            progressValuePm25[2] = progress+1;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    SeekBar.OnSeekBarChangeListener PM25_SeekBar_Listener_Bad = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress < 1) {
                progress = 1;
            }
            seekBar.setProgress(progress);
            progress += progressValuePm25[1];
            
            binding.layoutGradePm25.pm25TextViewBad.setText(String.valueOf(progress));
            progressValuePm25[2] = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}
