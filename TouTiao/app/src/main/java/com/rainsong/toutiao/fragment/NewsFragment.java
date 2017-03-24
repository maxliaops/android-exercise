package com.rainsong.toutiao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.rainsong.toutiao.animation.ScaleInAnimation;
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

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.rv_news_list)
    RecyclerView mNewsList;

    private String mCategory;
    private Context mContext;
    private NewsAdapter mAdapter;
    private NewsDataSource mNewsDataSource;
    private List<GroupInfoEntity> mDataList;
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

    public NewsFragment() {
        mDataList = new ArrayList<>();
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
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new NewsAdapter(mContext, mDataList);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroupInfoEntity groupInfo = mAdapter.getItemData(position);
                String articleUrl = groupInfo.getArticle_url();
                startNewsDetailActivity(articleUrl);
            }
        });
        mNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        mNewsList.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                initiateRefresh();
            }
        });

        Log.d(TAG, "onViewCreated(): category=" + mCategory);
        getFeedNews(mCategory);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initiateRefresh() {
        Log.i(TAG, "initiateRefresh");

        getFeedNews(mCategory);
    }

    private void onRefreshComplete() {
        Log.i(TAG, "onRefreshComplete");

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void startNewsDetailActivity(String articleUrl) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("article_url", articleUrl);
        mContext.startActivity(intent);
    }

    public void getFeedNews(final String category) {
        Log.i(TAG, "getFeedNews(): category=" + category);
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDataManager.getFeedArticleList(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArticleListResponseEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                        onRefreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError(): category=" + category + " error=" +
                                e.toString());
                        onRefreshComplete();
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
                                if (groupInfo != null && !TextUtils.isEmpty(groupInfo.getTitle())) {
//                                    Log.d(TAG, "onNext(): url=" + groupInfo.getArticle_url());
                                    groupInfos.add(groupInfo);
                                }
                            }
                            groupInfos.addAll(mDataList);
                            mDataList = groupInfos;
                            mAdapter.updateData(groupInfos);
                        }
                    }
                });
    }

}