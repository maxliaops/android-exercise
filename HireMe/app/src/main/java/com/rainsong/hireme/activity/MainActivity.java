package com.rainsong.hireme.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rainsong.hireme.R;
import com.rainsong.hireme.base.BaseActivity;
import com.rainsong.hireme.fragment.Hire2Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements BottomNavigationBar
        .OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private Hire2Fragment mHireFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        setDefaultFragment();
    }

    private void initView() {
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_main_news_normal, R.string.news)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_video_normal, R.string
                        .video)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_discover_normal, R
                        .string.discover)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_mine_normal, R.string.mine)
                        .setActiveColorResource(R.color.red))
                .initialise();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mHireFragment = new Hire2Fragment();
        transaction.add(R.id.fragment_container, mHireFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Timber.d("onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHireFragment == null) {
                    mHireFragment = new Hire2Fragment();
                    transaction.add(R.id.fragment_container, mHireFragment);
                } else {
                    transaction.attach(mHireFragment);
                }
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        Timber.d("onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }
}
