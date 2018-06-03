package com.rainsong.blockexplorer.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rainsong.blockexplorer.R;
import com.rainsong.blockexplorer.base.BaseActivity;
import com.rainsong.blockexplorer.fragment.BlockFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements BottomNavigationBar
        .OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private BlockFragment mBlockFragment;

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
                .addItem(new BottomNavigationItem(R.drawable.icon_main_news_normal, R.string.block)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_video_normal, R.string.transaction)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_discover_normal, R.string.address)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_mine_normal, R.string.mine)
                        .setActiveColorResource(R.color.red))
                .initialise();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mBlockFragment = new BlockFragment();
        transaction.add(R.id.fragment_container, mBlockFragment);
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
                if (mBlockFragment == null) {
                    mBlockFragment = new BlockFragment();
                    transaction.add(R.id.fragment_container, mBlockFragment);
                } else {
                    transaction.attach(mBlockFragment);
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