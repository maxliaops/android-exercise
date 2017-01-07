package com.rainsong.mishop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rainsong.mishop.adapter.SectionsPagerAdapter;
import com.rainsong.mishop.fragment.CatagoryFragment;
import com.rainsong.mishop.fragment.DiscoverFragment;
import com.rainsong.mishop.fragment.HomeFragment;
import com.rainsong.mishop.fragment.UserCentralFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar
        .OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        initBottomNavigationBar();
        initViewPager();
    }

    private void initBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_main_home_selected, R.string.home)
                        .setInactiveIconResource(R.drawable.icon_main_home_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_category_selected, R
                        .string.category)
                        .setInactiveIconResource(R.drawable.icon_main_category_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_discover_selected, R
                        .string.discover)
                        .setInactiveIconResource(R.drawable.icon_main_discover_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_mine_selected, R.string.mine)
                        .setInactiveIconResource(R.drawable.icon_main_mine_normal)
                        .setActiveColorResource(R.color.orange))
                .initialise();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new CatagoryFragment());
        fragments.add(new DiscoverFragment());
        fragments.add(new UserCentralFragment());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
