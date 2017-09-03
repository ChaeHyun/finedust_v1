package com.finedust.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.finedust.R;
import com.finedust.databinding.ActivitySearchAdressBinding;

public class SearchAdressActivity extends AppCompatActivity {
    ActivitySearchAdressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_adress);
        setContentView(R.layout.activity_search_adress);
    }
}
