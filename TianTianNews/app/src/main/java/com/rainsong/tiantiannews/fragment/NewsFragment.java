package com.rainsong.tiantiannews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainsong.tiantiannews.R;


/**
 * Created by maxliaops on 17-1-11.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String ARG_CATAGORY = "catagory";

    private String catagory;

    private TextView mTvCatagory;

    public static NewsFragment newInstance(String catagory) {
        Log.d(TAG, "newInstance(): catagory=" + catagory);
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATAGORY, catagory);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catagory = getArguments().getString(ARG_CATAGORY);
        Log.d(TAG, "onCreate(): catagory=" + catagory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView(): catagory=" + catagory);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mTvCatagory = (TextView) rootView.findViewById(R.id.tv_catagory);
        mTvCatagory.setText(catagory);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): catagory=" + catagory);
    }
}