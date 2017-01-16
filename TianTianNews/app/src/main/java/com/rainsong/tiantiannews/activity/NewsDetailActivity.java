package com.rainsong.tiantiannews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.entity.NewsListEntity.Result.NewsEntity;

/**
 * Created by maxliaops on 17-1-14.
 */

public class NewsDetailActivity extends Activity {
    private NewsEntity mNewsEntity;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        mNewsEntity = (NewsEntity)intent.getSerializableExtra("newsEntity");

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadUrl(mNewsEntity.url);
    }
}
