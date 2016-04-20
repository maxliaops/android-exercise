package com.rainsong.wejoke;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class JokeActivity extends Activity {
    private static final String TAG = "JokeActivity";

    private static final String JOKE_URL = "http://api.1-blog.com/biz/bizserver/xiaohua/list.do";

    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private JokeAdapter mAdapter;
    private Gson mGson;
    private Integer mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        mContext = this;
        mGson = new Gson();
        mPage = 0;
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        mPullToRefreshListView
                .setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(
                                getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        mPage++;
                        new GetDataTask(mContext).execute(mPage.toString());
                    }
                });

        ListView actualListView = mPullToRefreshListView.getRefreshableView();
        mAdapter = new JokeAdapter(mContext);
        actualListView.setAdapter(mAdapter);

        new GetDataTask(mContext).execute(mPage.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.joke, menu);
        return true;
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {
        Context mContext;

        public GetDataTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String targetUrl = JOKE_URL;
            String page = params[0];

            ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("size", "20"));
            paramList.add(new BasicNameValuePair("page", page));

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

            Log.d(TAG, "doInBackground(): targetUrl=" + targetUrl);
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

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray jsonArray = jsonResult.getJSONArray("detail");

                    List<JokeEntity> data = new ArrayList<JokeEntity>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        JokeEntity item = mGson.fromJson(jsonItem.toString(),
                                JokeEntity.class);
                        data.add(item);
                    }

                    mAdapter.addDataItems(data);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    }
}
