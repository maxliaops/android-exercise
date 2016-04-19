package com.rainsong.wejoke;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JokeAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<JokeEntity> mData;

    public JokeAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = new ArrayList<JokeEntity>();
    }

    public void addDataItems(List<JokeEntity> data) {
        mData.addAll(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JokeEntity item = mData.get(position);
        holder.text.setText(item.content);
        return convertView;
    }

    class ViewHolder {
        TextView text;
    }
}
