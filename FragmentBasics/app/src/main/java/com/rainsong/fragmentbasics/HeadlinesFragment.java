package com.rainsong.fragmentbasics;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * Created by maxliaops on 16-11-10.
 */

public class HeadlinesFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1;
        ListAdapter adapter = new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines);
        setListAdapter(adapter);
    }
}
