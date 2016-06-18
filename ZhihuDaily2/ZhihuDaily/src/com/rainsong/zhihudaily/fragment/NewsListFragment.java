package com.rainsong.zhihudaily.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rainsong.zhihudaily.R;
import com.rainsong.zhihudaily.activity.HireDetailsActivity;
import com.rainsong.zhihudaily.adapter.NewsAdapter;
import com.rainsong.zhihudaily.entity.ListJobbersResponseInfo;
import com.rainsong.zhihudaily.entity.ListJobbersResponseInfo.RetDataEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class NewsListFragment extends Fragment {
    private static final String TAG = "NewsListFragment";

    public static final String SERVER_URL = "http://123.56.205.35:2345";
    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mActualListView;
    private NewsAdapter mAdapter;
    private ArrayList<RetDataEntity> mNewsList = null;
    // 上次listView滚动到最下方时，itemId
    private int mListViewPreLast = 0;
    private int clientRand;
    private int number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_list, container,
                false);

        mContext = getActivity();
        mNewsList = new ArrayList<RetDataEntity>();
        mPullToRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.list);
        mPullToRefreshListView
                .setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(
                                mContext.getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);
                        getMoreNews();
                    }
                });

        mActualListView = mPullToRefreshListView.getRefreshableView();
        mActualListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (mListViewPreLast != lastItem) {
                        getMoreNews();
                        mListViewPreLast = lastItem;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
        mActualListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                    long j) {
                Intent intent = new Intent();
                intent.putExtra("jobber_id",
                        ((RetDataEntity) NewsListFragment.this.mNewsList
                                .get(i - 1)).getId());
                intent.putExtra("jobber", ((RetDataEntity) NewsListFragment.this.mNewsList.get(i - 1)).getUser_id().toString());
                Log.d(TAG, "jobber_id: " + ((RetDataEntity) NewsListFragment.this.mNewsList.get(i - 1)).getId().toString());
                Log.d(TAG, "jobber: " + ((RetDataEntity) NewsListFragment.this.mNewsList.get(i - 1)).getUser_id().toString());
                intent.setClass(NewsListFragment.this.getActivity(), HireDetailsActivity.class);
                NewsListFragment.this.startActivity(intent);
            }

            
        });

        getFirstNews();

        return view;
    }

    private void setAdapter(ArrayList<RetDataEntity> newsList) {
        if (mAdapter == null) {
            mAdapter = new NewsAdapter(mContext, newsList);
            mActualListView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(newsList);
        }
    }

    private boolean checkIsContentSame(String oldContent, String newContent) {

        if (TextUtils.isEmpty(oldContent) || TextUtils.isEmpty(newContent)) {
            return false;
        }

        return oldContent.equals(newContent);
    }

    private void getMoreNews() {
        number++;
        HashMap hashMap = new HashMap();
        hashMap.put("start", Integer.valueOf(number));
        hashMap.put("city", "深圳");
        hashMap.put("loading", "true");
        hashMap.put("sex", 2);
        hashMap.put("age", 1);
        hashMap.put("height", 1);
        hashMap.put("income", 1);
        hashMap.put("rand", String.valueOf(clientRand));

        String toJson = new Gson().toJson(hashMap);
        Log.d(TAG, "getMoreNews: " + toJson);
        String str = new String(Base64.encode(toJson.getBytes(), 0));
        Log.d(TAG, "getMoreNews--str: " + str);
        OkHttpUtils.get().url(SERVER_URL).addParams("c", "Jobbers")
                .addParams("m", "listJobbers").addParams("p", str).build()
                .execute(new MoreStringCallback());
    }

    private void getFirstNews() {
        number = 0;
        HashMap hashMap = new HashMap();
        hashMap.put("start", Integer.valueOf(number));
        hashMap.put("city", "深圳");
        hashMap.put("loading", "false");
        hashMap.put("sex", 2);
        hashMap.put("age", 1);
        hashMap.put("height", 1);
        hashMap.put("income", 1);

        String toJson = new Gson().toJson(hashMap);
        Log.d(TAG, "getFirstNews: " + toJson);
        String str = new String(Base64.encode(toJson.getBytes(), 0));
        Log.d(TAG, "getFirstNews--str: " + str);
        OkHttpUtils.get().url(SERVER_URL).addParams("c", "Jobbers")
                .addParams("m", "listJobbers").addParams("p", str).build()
                .execute(new FirstStringCallback());
    }

    public class FirstStringCallback extends StringCallback {
        public FirstStringCallback() {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d(TAG, "onError: " + e);
            mPullToRefreshListView.onRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d(TAG, "onResponse: " + response);
            try {
                JSONObject jSONObject = new JSONObject(response);
                String code = jSONObject.get("code").toString();
                if (code.equals("0")) {
                    ListJobbersResponseInfo info = (ListJobbersResponseInfo) new Gson()
                            .fromJson(response, ListJobbersResponseInfo.class);
                    clientRand = info.getData().getClientRand();

                    List<RetDataEntity> newsList = info.getData().getRetData();
                    mNewsList.addAll(newsList);
                    setAdapter(mNewsList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    public class MoreStringCallback extends StringCallback {
        public MoreStringCallback() {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d(TAG, "onError: " + e);
            mPullToRefreshListView.onRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d(TAG, "onResponse: " + response);
            try {
                JSONObject jSONObject = new JSONObject(response);
                String code = jSONObject.get("code").toString();
                if (code.equals("0")) {
                    ListJobbersResponseInfo info = (ListJobbersResponseInfo) new Gson()
                            .fromJson(response, ListJobbersResponseInfo.class);
                    List<RetDataEntity> newsList = info.getData().getRetData();
                    mNewsList.addAll(newsList);
                    setAdapter(mNewsList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    }

}
