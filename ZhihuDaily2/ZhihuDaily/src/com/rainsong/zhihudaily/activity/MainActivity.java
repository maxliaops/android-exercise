package com.rainsong.zhihudaily.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.rainsong.zhihudaily.R;
import com.rainsong.zhihudaily.fragment.NewsListFragment;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    private NewsListFragment mNewsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of NewsListFragment
            NewsListFragment mNewsListFragment = new NewsListFragment();

            // In case this activity was started with special instructions from
            // an Intent,
            // pass the Intent's extras to the fragment as arguments
            mNewsListFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mNewsListFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
