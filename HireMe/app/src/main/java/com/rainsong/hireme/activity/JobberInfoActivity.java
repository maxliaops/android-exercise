package com.rainsong.hireme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainsong.hireme.R;
import com.rainsong.hireme.adapter.PhotoPagerAdapter;
import com.rainsong.hireme.base.BaseActivity;
import com.rainsong.hireme.bean.JobberDetailInfo;
import com.rainsong.hireme.bean.JobberDetailInfo.DataEntity.MsgEntity;
import com.rainsong.hireme.bean.WechatInfo;
import com.rainsong.hireme.data.DataManager;
import com.rainsong.hireme.util.GsonUtil;
import com.rainsong.hireme.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-21.
 */

public class JobberInfoActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    TextView mIndicator;
    @BindView(R.id.get_wechat)
    Button mGetWechat;
    @BindView(R.id.summary)
    TextView mSummary;
    @BindView(R.id.wechat_account)
    TextView mWechatAccount;
    @BindView(R.id.info)
    TextView mInfo;

    private DataManager mDataManager;
    private String mUserId;
    private MsgEntity mJobberDetailInfo;
    private PhotoPagerAdapter mPhotoPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobber_info);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("user_id");
        mInfo.setText(mUserId);
        mPhotoPagerAdapter = new PhotoPagerAdapter(this);
        mViewPager.setAdapter(mPhotoPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mGetWechat.setOnClickListener(this);
        getDetailInfo();
    }

    private void getWechatInfo() {
        mDataManager.getJobberWechatInfo(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<WechatInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WechatInfo wechatInfo) {
                        if (wechatInfo != null) {
                            WechatInfo.DataEntity data = wechatInfo.getData();
                            if (data.getStatus() == 5) {
                                String wechatAccount = data.getWechat_account();
                                Timber.d("get wechat account: " + wechatAccount);
                                if (!StringUtils.isEmpty(wechatAccount)) {
                                    if (Patterns.WEB_URL.matcher(wechatAccount).matches() || data
                                            .getType() == 2) {
                                        mPhotoPagerAdapter.addData(wechatAccount);
                                        mWechatAccount.setText(wechatAccount);
                                    } else {
                                        mWechatAccount.setText(wechatAccount);
                                    }
                                }
                            } else {
                                String msg = data.getMsg();
                                Timber.d("get wechat msg: " + msg);
                                mWechatAccount.setText(msg);
                            }
                        }
                    }
                });
    }

    private void getDetailInfo() {
        mDataManager.getJobberDetailInfo(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JobberDetailInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JobberDetailInfo jobberDetailInfo) {
                        if (jobberDetailInfo != null) {
                            mJobberDetailInfo = jobberDetailInfo.getData().getMsg().get(0);
                            List<String> photoUrls = mJobberDetailInfo.getImg();
                            mPhotoPagerAdapter.addData(photoUrls);
                            int rentNum = mJobberDetailInfo.getRent_num();
                            int price = mJobberDetailInfo.getPrice();
                            String age = mJobberDetailInfo.getAge();
                            String job = mJobberDetailInfo.getJob();
                            int gender = mJobberDetailInfo.getGender();
                            String summary = rentNum + "次, " + price + ", " + age + "岁";
                            if (!StringUtils.isEmpty(job)) {
                                summary += ", " + job;
                            }
                            if (gender == 1) {
                                summary += ", 男";
                            }
                            mSummary.setText(summary);
                            String info = GsonUtil.stringToJSON(GsonUtil.getGson().toJson
                                    (mJobberDetailInfo, MsgEntity.class).toString());
                            mInfo.setText(info);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int count = mPhotoPagerAdapter.getCount();
        String indicator = String.valueOf(position + 1) + "/" + String.valueOf(count);
        mIndicator.setText(indicator);
    }

    @Override
    public void onPageSelected(int position) {
        int count = mPhotoPagerAdapter.getCount();
        String indicator = String.valueOf(position + 1) + "/" + String.valueOf(count);
        mIndicator.setText(indicator);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_wechat:
                getWechatInfo();
                break;
        }
    }
}
