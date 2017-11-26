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

public class WebPageKaq extends Fragment {
    private FragmentWebviewBinding binding;
    private String url = "http://www.kaq.or.kr/m.html#anima";

    public WebPageKaq() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        binding.textViewFrom.setText("*위 자료는 안양대학교 기후융합연구소(소장:환경에너지공학과 구윤서 교수)에서 제공하는 예측자료이며 실시간으로 측정된 자료는 아닙니다.");
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
