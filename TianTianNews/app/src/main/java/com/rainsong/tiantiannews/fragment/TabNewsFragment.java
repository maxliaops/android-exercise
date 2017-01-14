package com.rainsong.tiantiannews.fragment;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

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
            catalogs.add("top");
            catalogs.add("shehui");
            catalogs.add("guonei");
            catalogs.add("guoji");
            catalogs.add("yule");
            catalogs.add("tiyu");
            catalogs.add("junshi");
            catalogs.add("keji");
            catalogs.add("caijing");
            catalogs.add("shishang");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String category = catalogs.get(position);
            String pageTitle;
            switch (category) {
                case "top":
                    pageTitle = getString(R.string.category_top);
                    break;
                case "shehui":
                    pageTitle = getString(R.string.category_shehui);
                    break;
                case "guonei":
                    pageTitle = getString(R.string.category_guonei);
                    break;
                case "guoji":
                    pageTitle = getString(R.string.category_guoji);
                    break;
                case "yule":
                    pageTitle = getString(R.string.category_yule);
                    break;
                case "tiyu":
                    pageTitle = getString(R.string.category_tiyu);
                    break;
                case "junshi":
                    pageTitle = getString(R.string.category_junshi);
                    break;
                case "keji":
                    pageTitle = getString(R.string.category_keji);
                    break;
                case "caijing":
                    pageTitle = getString(R.string.category_caijing);
                    break;
                case "shishang":
                    pageTitle = getString(R.string.category_shishang);
                    break;
                default:
                    pageTitle = getString(R.string.category_top);
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
