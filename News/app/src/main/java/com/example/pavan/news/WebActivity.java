package com.example.pavan.news;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class WebActivity extends AppCompatActivity {
    WebView webView;

    @Override
    public void onBackPressed() {

        finishActivity(101);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setFixedFontFamily("sans-serif-condensed-light");
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
