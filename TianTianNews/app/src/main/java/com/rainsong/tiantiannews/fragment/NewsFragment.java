package com.rainsong.tiantiannews.fragment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rainsong.tiantiannews.R;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";
    public static final String URL_NEWS = "http://v.juhe.cn/toutiao/index";
    private static final String JUHE_APPKEY = "4e1d849f4ee325ef117b324d2c834ff2";

    private String mCategory;
    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mActualListView;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;

    private String[] mStrings = {"John", "Michelle", "Amy", "Kim", "Mary",
            "David", "Sunny", "James", "Maria", "Michael", "Sarah", "Robert",
            "Lily", "William", "Jessica", "Paul", "Crystal", "Peter",
            "Jennifer", "George", "Rachel", "Thomas", "Lisa", "Daniel", "Elizabeth",
            "Kevin"};

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

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                mListItems);
        mActualListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private String httpGet(String targetUrl) {
        HttpGet httpRequest = new HttpGet(targetUrl);
        try {

            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                return strResult;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0)
                return null;
            String category = params[0];

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
            Log.d(TAG, "GetNewsTask(): result=" + result);
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

}