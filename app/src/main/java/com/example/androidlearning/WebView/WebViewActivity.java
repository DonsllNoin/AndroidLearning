package com.example.androidlearning.WebView;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.androidlearning.R;

public class WebViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.web_view);

        // 设置一些浏览器的属性，支持 JavaScript 脚本
        webView.getSettings().setJavaScriptEnabled(true);

        // 需要从一个网页跳转到另一个网页时，希望仍然在当前的 WebView 中显示，为不是打开浏览器
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://www.baidu.com/");

    }
}