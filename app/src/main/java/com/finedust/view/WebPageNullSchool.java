package com.finedust.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.finedust.R;
import com.finedust.databinding.FragmentWebviewBinding;

public class WebPageNullSchool extends Fragment {
    private FragmentWebviewBinding binding;
    private String url = "https://earth.nullschool.net/#current/particulates/surface/level/overlay=pm10/orthographic=-233.47,40.95,1034";

    public WebPageNullSchool() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        binding.textViewFrom.setText("* earth.nullschool.net에서 제공하는 정보입니다. \n  실시간 대기의 흐름을 확인 할 수 있습니다.");
        setProgressBar(true);
        getWebPage(url);
        setProgressBar(false);

        return binding.getRoot();
    }

    private void getWebPage(String url) {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.setWebViewClient(new WebViewClient());
        try {
            binding.webView.loadUrl(url);
        }
        catch (Exception e) {
            setProgressBar(false);
        }
    }

    private void setProgressBar(boolean on) {
        if (on) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.webView.setVisibility(View.INVISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.webView.setVisibility(View.VISIBLE);
        }
    }
}
