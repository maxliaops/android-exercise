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
import com.rainsong.tiantiannews.data.DataManager;
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
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";

    @BindView(R.id.rv_news_list)
    RecyclerView rvNewsList;

    private String mCategory;
    private Context mContext;
    private NewsAdapter mAdapter;
    private List<DataBean> mNewsList;
    private NewsDataSource mNewsDataSource;
    private DataManager mDataManager;
    private Subscription mSubscription;

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
        mDataManager = DataManager.getInstance();
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
        getNews(mCategory);
    }

    private void startNewsDetailActivity(DataBean newsBean) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("newsBean", newsBean);
        mContext.startActivity(intent);
    }

    public void getNews(final String category) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDataManager.getNews(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsListBean newsListBean) {
                        Log.d(TAG, "onNext(): category=" + category + " result=" + newsListBean
                                .getReason());
                        if (newsListBean.getErrorCode() == 0) {
                            List<DataBean> newsList = newsListBean.getResult().getData();
                            for (DataBean newsBean : newsList) {
                                newsBean.setCategory(category);
                                mNewsDataSource.saveNews(newsBean);
                            }
                            mAdapter.updateData(newsList);
                        }
                    }
                });
    }

}