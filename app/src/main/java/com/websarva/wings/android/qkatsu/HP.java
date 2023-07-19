package com.websarva.wings.android.qkatsu;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class HP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hp);


        String url = getIntent().getStringExtra("URL");
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}