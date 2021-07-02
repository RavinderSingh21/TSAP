package com.example.newtsap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;

public class MeasuresActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_measures);
        Toolbar toolbar = findViewById(R.id.toolbarm);
        setSupportActionBar(toolbar);
   //     setTitle(R.string.Safety_Measures);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        WebView webView =findViewById(R.id.webView);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ms = new Intent(MeasuresActivity.this,MapsActivity.class);
                startActivity(intent_ms);
                finish();

            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/safety.html");
    //    webView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#FFFFFF\");");
   //     webView.setBackgroundColor(Color.parseColor("#008577"));



    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();
    }
}