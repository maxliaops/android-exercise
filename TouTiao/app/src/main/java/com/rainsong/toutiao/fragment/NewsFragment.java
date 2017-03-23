package com.rainsong.toutiao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainsong.toutiao.R;
import com.rainsong.toutiao.activity.NewsDetailActivity;
import com.rainsong.toutiao.adapter.NewsAdapter;
import com.rainsong.toutiao.data.DataManager;
import com.rainsong.toutiao.data.NewsDataSource;
import com.rainsong.toutiao.entity.ArticleListResponseEntity;
import com.rainsong.toutiao.entity.ArticleListResponseEntity.DataEntity;
import com.rainsong.toutiao.entity.GroupInfoEntity;
import com.rainsong.toutiao.util.GsonUtils;

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
//        Log.d(TAG, "onCreate(): category=" + mCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
//        Log.d(TAG, "onCreateView(): category=" + mCategory);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);
        mAdapter = new NewsAdapter(mContext, null);
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroupInfoEntity groupInfo = mAdapter.getItemData(position);
                String articleUrl = groupInfo.getArticle_url();
                startNewsDetailActivity(articleUrl);
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
        getFeedNews(mCategory);
    }

    private void startNewsDetailActivity(String articleUrl) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("article_url", articleUrl);
        mContext.startActivity(intent);
    }

    public void getFeedNews(final String category) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDataManager.getFeedArticleList(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArticleListResponseEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError(): category=" + category + " error=" +
                                e.toString());
                    }

                    @Override
                    public void onNext(ArticleListResponseEntity articleListResponseEntity) {
                        Log.d(TAG, "onNext(): category=" + category + " result=" +
                                articleListResponseEntity.getTotal_number());
                        if (articleListResponseEntity.getMessage().equals("success")) {
                            List<DataEntity> dataList = articleListResponseEntity.getData();
                            List<GroupInfoEntity> groupInfos = new ArrayList<GroupInfoEntity>();
                            for (DataEntity dataEntity : dataList) {
                                String content = dataEntity.getContent();
                                GroupInfoEntity groupInfo = (GroupInfoEntity) GsonUtils.getEntity
                                        (content, GroupInfoEntity.class);
                                if(groupInfo != null && !TextUtils.isEmpty(groupInfo.getTitle())) {
                                    Log.d(TAG, "onNext(): url=" + groupInfo.getArticle_url());
                                    groupInfos.add(groupInfo);
                                }
                            }
                            mAdapter.updateData(groupInfos);
                        }
                    }
                });
    }

}