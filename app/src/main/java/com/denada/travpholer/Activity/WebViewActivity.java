package com.denada.travpholer.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.denada.travpholer.R;

import java.util.Calendar;

/**
 * Created by hgc on 6/10/2016.
 */
public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String link;
    long firsttime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Calendar calendar = Calendar.getInstance();
        firsttime = calendar.getTimeInMillis();

        webView = (WebView)findViewById(R.id.webview);

//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >=19){
            webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }else{
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        Intent intent = getIntent();

        try{
            link = intent.getStringExtra("url");

        }catch (Exception ex){

        }
        if (link == null){
            finish();
            return;
        }

        webView.loadUrl(link);
    }
}
