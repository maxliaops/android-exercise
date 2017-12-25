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

import com.rainsong.hireme.R;
import com.rainsong.hireme.activity.JobberInfoActivity;
import com.rainsong.hireme.adapter.JobberAdapter;
import com.rainsong.hireme.base.BaseFragment;
import com.rainsong.hireme.bean.HireFragmentInfo;
import com.rainsong.hireme.bean.HireFragmentInfo.DataEntityX.DataEntity.MemberEntity;
import com.rainsong.hireme.data.DataManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-20.
 */

public class HireFragment extends BaseFragment implements JobberAdapter.OnItemClickListener {
    public static final String FILE_HIRE_MEMBERS_ASSET = "hire_members.json";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Context mContext;
    private DataManager mDataManager;
    private JobberAdapter mAdapter;
    private HireFragmentInfo mHireFragmentInfo;
    private List<MemberEntity> mMembers;

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
        mAdapter = new JobberAdapter(mContext, null);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerView.setAdapter(mAdapter);
        getMembers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getMembers() {
        mDataManager.getMembers(mContext)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<HireFragmentInfo>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HireFragmentInfo hireFragmentInfo) {
                        mHireFragmentInfo = hireFragmentInfo;
                        mMembers = mHireFragmentInfo.getData().getData().getMember();
                        Timber.d("jobber members count: " + mMembers.size());
                        List<MemberEntity> members = new ArrayList<MemberEntity>();
                        int i = 0;
                        for(i = 0; i < 55; i++) {
                            members.add(mMembers.get(i));
                        }
                        mAdapter.setData(mMembers);
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        MemberEntity member = mAdapter.getItemData(position);
        String userId = member.getUser_id();
        startJobberInfoActivity(userId);
    }

    private void startJobberInfoActivity(String userId) {
        Intent intent = new Intent(mContext, JobberInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user_id", userId);
        mContext.startActivity(intent);
    }
}
