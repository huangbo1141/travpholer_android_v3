package com.denada.travpholer.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.denada.travpholer.R;
import com.denada.travpholer.adapter.AutoCompleteAdapter;
import com.denada.travpholer.view.autocomplete.rest.model.Prediction;

import java.util.Calendar;
import java.util.Date;

public class WebFragment extends BaseFragment implements View.OnClickListener {

    ProgressBar progressBar;
    TextView txtLocation;
    AutoCompleteAdapter mAutoCompleteAdapter;
    Prediction mPrediction;
    long firsttime;
    WebView webView;
    String link;
    private View mRootView;
    private View view_takephoto, view_selectphoto;
    private EditText edtTitle, edtLink;
    private ImageView img_marker, img_pic;
    private View view_submit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onCreateView", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onCreateView", "onCreateView");


        mRootView = inflater.inflate(R.layout.activity_webview, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        firsttime = calendar.getTimeInMillis();

        webView = (WebView) mRootView.findViewById(R.id.webview);

//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return false;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                progressBar.setVisibility(View.GONE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                long end = calendar.getTimeInMillis();
                Log.e("seconds ", String.valueOf(end - firsttime));
                super.onPageFinished(view, url);
            }
        });


        if (link != null) {
            Log.e("seconds %s", link);
            webView.loadUrl(link);
        }
        setCaption("", R.drawable.ico_back);
        return mRootView;
    }

    @Override
    public void onDetach() {

        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            link = this.getArguments().getString("url");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }
    }
}
