package com.rainsong.tiantiannews.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.activity.NewsDetailActivity;
import com.rainsong.tiantiannews.adapter.NewsAdapter;
import com.rainsong.tiantiannews.bean.NewsListBean;
import com.rainsong.tiantiannews.bean.NewsListBean.ResultBean.DataBean;
import com.rainsong.tiantiannews.data.NewsDataSource;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";
    private static final String URL_NEWS = "http://v.juhe.cn/toutiao/index";
    private static final String JUHE_APPKEY = "4e1d849f4ee325ef117b324d2c834ff2";

    @BindView(R.id.rv_news_list)
    RecyclerView rvNewsList;

    private String mCategory;
    private Context mContext;
    private NewsAdapter mAdapter;
    private List<DataBean> mNewsList;
    private NewsDataSource mNewsDataSource;

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
        mNewsDataSource = NewsDataSource.getInstance(getContext());
        Log.d(TAG, "onCreate(): category=" + mCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView(): category=" + mCategory);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);
        mNewsList = new ArrayList<>();
        mAdapter = new NewsAdapter(mContext, mNewsList);
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DataBean newsBean = mAdapter.getItemData(position);
                startNewsDetailActivity(newsBean);
            }
        });
        rvNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        rvNewsList.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): category=" + mCategory);
        new GetNewsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCategory);
    }

    private void startNewsDetailActivity(DataBean newsBean) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("newsBean", newsBean);
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
                NewsListBean newsListBean = (NewsListBean) GsonUtils.getEntity(result,
                        NewsListBean.class);
                Log.d(TAG, "GetNewsTask(): category=" + mCategory + " result=" + newsListBean
                        .getReason());
                if (newsListBean.getErrorCode() == 0) {
                    List<DataBean> newsList = newsListBean.getResult().getData();
                    for (DataBean newsBean : newsList) {
                        newsBean.setCategory(mCategory);
                        mNewsDataSource.saveNews(newsBean);
                    }
                    mAdapter.updateData(newsList);
                }
            }

            super.onPostExecute(result);
        }
    }

}