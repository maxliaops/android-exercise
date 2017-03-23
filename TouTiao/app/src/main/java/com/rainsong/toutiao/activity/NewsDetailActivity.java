package com.rainsong.toutiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.rainsong.toutiao.R;
import com.rainsong.toutiao.bean.NewsListBean.ResultBean.DataBean;

/**
 * Created by maxliaops on 17-1-14.
 */

public class NewsDetailActivity extends AppCompatActivity {
    private String mArticleUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mArticleUrl = intent.getStringExtra("article_url");

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadUrl(mArticleUrl);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
