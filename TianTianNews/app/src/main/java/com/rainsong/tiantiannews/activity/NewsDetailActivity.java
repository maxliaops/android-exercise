package com.rainsong.tiantiannews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.entity.NewsListEntity.Result.NewsEntity;

/**
 * Created by maxliaops on 17-1-14.
 */

public class NewsDetailActivity extends AppCompatActivity {
    private NewsEntity mNewsEntity;
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
        mNewsEntity = (NewsEntity) intent.getSerializableExtra("newsEntity");

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadUrl(mNewsEntity.url);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
