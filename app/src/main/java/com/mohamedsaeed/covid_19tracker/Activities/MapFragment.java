package com.mohamedsaeed.covid_19tracker.Activities;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.mohamedsaeed.covid_19tracker.R;


public class MapFragment extends Fragment {
    private WebView webView;
    private ProgressBar progressBar;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //init views
        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        initWebView();

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        // Configure related browser settings

        //Sets whether the WebView should load image resources.
        webView.getSettings().setLoadsImagesAutomatically(true);
        //Tells the WebView to enable JavaScript execution.
        webView.getSettings().setJavaScriptEnabled(true);
        //Specify the style of the scrollbars
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        webView.setWebViewClient(new WebViewClient());


        //Sets whether the DOM storage API is enabled. make app faster, disabled by default for space savings and security
        webView.getSettings().setDomStorageEnabled(true);
        //Sets whether the WebView should support zooming using its on-screen zoom controls and gestures.
        webView.getSettings().setSupportZoom(true);
        //Sets whether the WebView should use its built-in zoom mechanisms.
        webView.getSettings().setBuiltInZoomControls(true);
        //Sets whether the WebView should display on-screen zoom controls when using the built-in zoom mechanisms.
        webView.getSettings().setDisplayZoomControls(true);

        // Load the initial URL
        webView.loadUrl("https://www.bing.com/covid");
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);

        }
    }

}
