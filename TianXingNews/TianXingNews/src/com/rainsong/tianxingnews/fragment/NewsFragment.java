package com.rainsong.tianxingnews.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.rainsong.tianxingnews.R;
import com.rainsong.tianxingnews.adapter.NewsAdapter;
import com.rainsong.tianxingnews.entity.NewsListEntity;
import com.rainsong.tianxingnews.entity.NewsListEntity.NewsEntity;
import com.rainsong.tianxingnews.util.GsonUtils;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";

    private static final String ARG_POSITION = "position";

    private Context mContext;
    private ListView mListView;
    private NewsAdapter mAdapter;
    private ArrayList<NewsEntity> mNewsList = null;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mContext = getActivity();

        mListView = (ListView) view.findViewById(R.id.list);
        Log.d(TAG, "onCreateView(): mListView=" + mListView);

        if (position == 0) {
            apiTest();
        }
        return view;
    }

    private void setAdapter(ArrayList<NewsEntity> newsList) {
        if (mAdapter == null) {
            mAdapter = new NewsAdapter(mContext, newsList);
            Log.d(TAG, "setAdapter(): mListView=" + mListView);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(newsList);
        }
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
                        Log.i(TAG,
                                "NewsListEntity: "
                                        + newsListEntity.newslist.size());
                        // mTextView.setText(responseString);
                        mNewsList = new ArrayList<NewsEntity>();
                        mNewsList.addAll(newsListEntity.newslist);
                        setAdapter(mNewsList);
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