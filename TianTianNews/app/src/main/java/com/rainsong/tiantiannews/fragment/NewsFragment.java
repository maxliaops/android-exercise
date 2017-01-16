package com.rainsong.tiantiannews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.activity.NewsDetailActivity;
import com.rainsong.tiantiannews.adapter.NewsAdapter;
import com.rainsong.tiantiannews.entity.NewsListEntity;
import com.rainsong.tiantiannews.entity.NewsListEntity.Result.NewsEntity;
import com.rainsong.tiantiannews.util.GsonUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";
    private static final String URL_NEWS = "http://v.juhe.cn/toutiao/index";
    private static final String JUHE_APPKEY = "4e1d849f4ee325ef117b324d2c834ff2";

    private String mCategory;
    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mActualListView;
    private NewsAdapter mAdapter;
    private List<NewsEntity> mNewsList;

    public static NewsFragment newInstance(String category) {
        Log.d(TAG, "newInstance(): category=" + category);
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATAGORY, category);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategory = getArguments().getString(ARG_CATAGORY);
        Log.d(TAG, "onCreate(): category=" + mCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView(): category=" + mCategory);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mContext = getActivity();
        mPullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.news_listview);
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mContext, System
                                .currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils
                                .FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new GetNewsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCategory);
            }
        });
        mActualListView = mPullToRefreshListView.getRefreshableView();

        mNewsList = new ArrayList<>();
        mAdapter = new NewsAdapter(mContext, mNewsList);
        mActualListView.setAdapter(mAdapter);
        mActualListView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): category=" + mCategory);
        new GetNewsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCategory);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsEntity newsEntity = (NewsEntity) parent.getItemAtPosition(position);
        startNewsDetailActivity(newsEntity);
    }

    private void startNewsDetailActivity(NewsEntity newsEntity) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("newsEntity", newsEntity);
        mContext.startActivity(intent);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    private String httpGet(String targetUrl) {
        String data = "";
        HttpURLConnection httpUrlConnection = null;

        try {
            httpUrlConnection = (HttpURLConnection) new URL(targetUrl)
                    .openConnection();

            InputStream in = new BufferedInputStream(
                    httpUrlConnection.getInputStream());

            data = readStream(in);

        } catch (MalformedURLException exception) {
            Log.e(TAG, "MalformedURLException");
        } catch (IOException exception) {
            Log.e(TAG, "IOException");
        } finally {
            if (null != httpUrlConnection)
                httpUrlConnection.disconnect();
        }
        return data;
    }

    private class GetNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0)
                return null;
            String category = params[0];
            Log.d(TAG, "GetNewsTask(): category=" + category);

            String targetUrl = URL_NEWS;

            ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("key", JUHE_APPKEY));
            paramList.add(new BasicNameValuePair("type", category));

            for (int i = 0; i < paramList.size(); i++) {
                NameValuePair nowPair = paramList.get(i);
                String value = nowPair.getValue();
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (Exception e) {
                }
                if (i == 0) {
                    targetUrl += ("?" + nowPair.getName() + "=" + value);
                } else {
                    targetUrl += ("&" + nowPair.getName() + "=" + value);
                }
            }

            return httpGet(targetUrl);
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.d(TAG, "GetNewsTask(): result=" + result);
            if (!TextUtils.isEmpty(result)) {
                NewsListEntity newsListEntity = (NewsListEntity) GsonUtils.getEntity(result,
                        NewsListEntity.class);
                Log.d(TAG, "GetNewsTask(): category=" + mCategory + " result=" + newsListEntity
                        .reason);
                if(newsListEntity.error_code == 0) {
                    List<NewsEntity> newsList = newsListEntity.result.getData();
                    mAdapter.updateData(newsList);
                }
            }

            // Call onRefreshComplete when the list has been refreshed.
            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

}