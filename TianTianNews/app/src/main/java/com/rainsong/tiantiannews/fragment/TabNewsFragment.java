package com.rainsong.tiantiannews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.widget.CategoryTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxliaops on 17-1-10.
 */

public class TabNewsFragment extends Fragment {
    private static final String TAG = "TabNewsFragment";
    private Activity mActivity;
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private NewsPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_news, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        tabs = (CategoryTabStrip) mActivity.findViewById(R.id.category_strip);
        pager = (ViewPager) mActivity.findViewById(R.id.view_pager);
        adapter = new NewsPagerAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> catalogs = new ArrayList<String>();

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add(getString(R.string.category_top));
            catalogs.add(getString(R.string.category_shehui));
            catalogs.add(getString(R.string.category_guonei));
            catalogs.add(getString(R.string.category_guoji));
            catalogs.add(getString(R.string.category_yule));
            catalogs.add(getString(R.string.category_tiyu));
            catalogs.add(getString(R.string.category_junshi));
            catalogs.add(getString(R.string.category_keji));
            catalogs.add(getString(R.string.category_caijing));
            catalogs.add(getString(R.string.category_shishang));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem(): position=" + position);
            String catalog = catalogs.get(position);
            return NewsFragment.newInstance(catalog);
        }

    }
}
