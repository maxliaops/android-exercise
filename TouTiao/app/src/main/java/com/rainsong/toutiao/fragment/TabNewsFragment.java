package com.rainsong.toutiao.fragment;

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

import com.rainsong.toutiao.R;
import com.rainsong.toutiao.widget.CategoryTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maxliaops on 17-1-10.
 */

public class TabNewsFragment extends Fragment {
    private static final String TAG = "TabNewsFragment";

    private CategoryTabStrip tabs;
    private ViewPager pager;
    private NewsPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_news, container, false);
        tabs = (CategoryTabStrip) rootView.findViewById(R.id.category_strip);
        pager = (ViewPager) rootView.findViewById(R.id.view_pager);
        adapter = new NewsPagerAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> catalogs = new ArrayList<String>();
        private Map<String, NewsFragment> fragmentMap = new HashMap<>();

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add("news_hot");
            catalogs.add("news_society");
            catalogs.add("news_entertainment");
            catalogs.add("news_tech");
            catalogs.add("news_sports");
            catalogs.add("news_car");
            catalogs.add("news_finance");
            catalogs.add("news_military");
            catalogs.add("news_world");
            catalogs.add("news_fashion");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String category = catalogs.get(position);
            String pageTitle;
            switch (category) {
                case "news_hot":
                    pageTitle = getString(R.string.category_news_hot);
                    break;
                case "news_society":
                    pageTitle = getString(R.string.category_news_society);
                    break;
                case "news_image":
                    pageTitle = getString(R.string.category_news_image);
                    break;
                case "news_world":
                    pageTitle = getString(R.string.category_news_world);
                    break;
                case "news_entertainment":
                    pageTitle = getString(R.string.category_news_entertainment);
                    break;
                case "news_sports":
                    pageTitle = getString(R.string.category_news_sports);
                    break;
                case "news_military":
                    pageTitle = getString(R.string.category_news_military);
                    break;
                case "news_tech":
                    pageTitle = getString(R.string.category_news_tech);
                    break;
                case "news_finance":
                    pageTitle = getString(R.string.category_news_finance);
                    break;
                case "news_fashion":
                    pageTitle = getString(R.string.category_news_fashion);
                    break;
                default:
                    pageTitle = getString(R.string.category_news_hot);
            }
            return pageTitle;
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem(): position=" + position);
            String catalog = catalogs.get(position);
            NewsFragment newsFragment = fragmentMap.get(catalog);
            if(newsFragment == null) {
                newsFragment = NewsFragment.newInstance(catalog);
                fragmentMap.put(catalog, newsFragment);
            }
            return newsFragment;
        }

    }
}
