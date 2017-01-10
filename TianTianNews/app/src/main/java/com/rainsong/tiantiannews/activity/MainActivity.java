package com.rainsong.tiantiannews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rainsong.tiantiannews.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar
        .OnTabSelectedListener {
    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_main_news_selected, R.string.news)
                        .setInactiveIconResource(R.drawable.icon_main_news_normal)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_video_selected, R
                        .string.video)
                        .setInactiveIconResource(R.drawable.icon_main_video_normal)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_discover_selected, R
                        .string.discover)
                        .setInactiveIconResource(R.drawable.icon_main_discover_normal)
                        .setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_mine_selected, R.string.mine)
                        .setInactiveIconResource(R.drawable.icon_main_mine_normal)
                        .setActiveColorResource(R.color.red))
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
