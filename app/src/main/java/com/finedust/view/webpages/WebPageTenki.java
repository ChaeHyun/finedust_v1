package com.finedust.view.webpages;


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

public class WebPageTenki extends Fragment {
    private FragmentWebviewBinding binding;
    private String url = "http://www.tenki.jp/particulate_matter/";

    public WebPageTenki() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        binding.textViewFrom.setText("* 일본 기상협회에서 제공하는 정보입니다.");
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
