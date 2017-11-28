package com.finedust.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;

import com.finedust.R;

public class Splash extends AppCompatActivity {
    private final int DELAY_TIME = 1500;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new handler_splash(), DELAY_TIME);
    }

    private class handler_splash implements Runnable {
        @Override
        public void run() {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            Splash.this.finish();
        }
    }
}
