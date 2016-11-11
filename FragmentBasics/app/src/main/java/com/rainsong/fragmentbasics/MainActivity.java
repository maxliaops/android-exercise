package com.rainsong.fragmentbasics;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity
        implements HeadlinesFragment.OnHeadlineSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate(): savedInstanceState=" + savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }

        HeadlinesFragment headlinesFragment = new HeadlinesFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, headlinesFragment).commit();
    }

    @Override
    public void onArticleSelected(int position) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ArticleFragment.ARG_POSITION, position);
        articleFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, articleFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
