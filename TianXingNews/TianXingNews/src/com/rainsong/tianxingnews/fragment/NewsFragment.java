package com.rainsong.tianxingnews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.rainsong.tianxingnews.entity.NewsListEntity;
import com.rainsong.tianxingnews.util.GsonUtils;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";

    private static final String ARG_POSITION = "position";

    private int position;

    public static NewsFragment newInstance(int position) {
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);

        if (position == 0) {
            apiTest();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        final int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                        .getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setText("PAGE " + (position + 1));

        fl.addView(v);
        return fl;
    }

    private void apiTest() {
        Log.i(TAG, "apiTest()");
        Parameters para = new Parameters();
        para.put("num", "10");
        para.put("page", "1");

        ApiStoreSDK.execute("http://apis.baidu.com/txapi/social/social",
                ApiStoreSDK.GET, para, new ApiCallBack() {

                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i(TAG, "onSuccess: " + responseString);
                        NewsListEntity newsListEntity = (NewsListEntity) GsonUtils
                                .getEntity(responseString, NewsListEntity.class);
                        Log.i(TAG, "NewsListEntity: " + newsListEntity.newslist.size());
                        // mTextView.setText(responseString);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString,
                            Exception e) {
                        Log.i(TAG, "onError, status: " + status);
                        Log.i(TAG,
                                "errMsg: " + (e == null ? "" : e.getMessage()));
                    }

                });

    }
}