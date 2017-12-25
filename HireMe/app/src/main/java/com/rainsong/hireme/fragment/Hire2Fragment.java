package com.rainsong.hireme.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rainsong.hireme.R;
import com.rainsong.hireme.activity.JobberInfoActivity;
import com.rainsong.hireme.adapter.JobbersListAdapter;
import com.rainsong.hireme.base.BaseFragment;
import com.rainsong.hireme.bean.JobbersListInfo;
import com.rainsong.hireme.bean.JobbersListInfo.DataEntity.RetDataEntity;
import com.rainsong.hireme.data.DataManager;
import com.rainsong.hireme.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.os.Build.ID;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PREFIX;

/**
 * Created by maxliaops on 17-12-20.
 */

public class Hire2Fragment extends BaseFragment implements JobbersListAdapter
        .OnItemClickListener, View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.load_more)
    Button mLoadMore;

    private Context mContext;
    private DataManager mDataManager;
    private JobbersListAdapter mAdapter;
    private int index = 0;
    private String mFreshId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hire, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new JobbersListAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerView.setAdapter(mAdapter);
        mLoadMore.setOnClickListener(this);
        mFreshId = newFreshId();
        getJobbersListInfo(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String newFreshId() {
        long time = System.currentTimeMillis();
        return StringUtils.randomString(4) + Long.toString(time);
    }

    private void getJobbersListInfo(boolean loading) {
        mDataManager.getJobbersListInfo(mFreshId, index, loading)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JobbersListInfo>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JobbersListInfo jobbersListInfo) {
                        if (jobbersListInfo != null) {
                            List<RetDataEntity> jobbers = jobbersListInfo.getData().getRetData();
                            if (jobbers != null && jobbers.size() > 0) {
                                mAdapter.addData(jobbers);
                            } else {

                            }
                        }
                    }
                });
        index++;
    }

    @Override
    public void onItemClick(int position) {
        RetDataEntity member = mAdapter.getItemData(position);
        String userId = member.getUser_id();
        startJobberInfoActivity(userId);
    }

    private void startJobberInfoActivity(String userId) {
        Intent intent = new Intent(mContext, JobberInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user_id", userId);
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_more:
                getJobbersListInfo(true);
                break;
        }
    }
}
