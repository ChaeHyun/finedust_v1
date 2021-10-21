package ch.breatheinandout.view.webpages;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import ch.breatheinandout.R;
import ch.breatheinandout.databinding.FragmentWebviewBinding;

public class WebPageAQI extends Fragment {
    private FragmentWebviewBinding binding;
    private String url = "http://aqicn.org/here";

    public WebPageAQI() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        binding.textViewFrom.setText("");
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
