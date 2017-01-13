package com.rainsong.tiantiannews.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rainsong.tiantiannews.R;

import java.util.Arrays;
import java.util.LinkedList;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";

    private String category;
    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mActualListView;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;

    private String[] mStrings = {"John", "Michelle", "Amy", "Kim", "Mary",
            "David", "Sunny", "James", "Maria", "Michael", "Sarah", "Robert",
            "Lily", "William", "Jessica", "Paul", "Crystal", "Peter",
            "Jennifer", "George", "Rachel", "Thomas", "Lisa", "Daniel", "Elizabeth",
            "Kevin"};

    public static NewsFragment newInstance(String category) {
        Log.d(TAG, "newInstance(): category=" + category);
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATAGORY, category);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = getArguments().getString(ARG_CATAGORY);
        Log.d(TAG, "onCreate(): category=" + category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView(): category=" + category);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mContext = getActivity();
        mPullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.news_listview);
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mContext, System
                                .currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils
                                .FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        mActualListView = mPullToRefreshListView.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mListItems);
        mActualListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

}