package com.rainsong.zhihudaily;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainsong.zhihudaily.NewsListEntity.NewsEntity;

public class NewsAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<NewsEntity> mDataList;

    public NewsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataList = new ArrayList<NewsEntity>();

    }

    public void addDataItems(List<NewsEntity> data) {
        mDataList.addAll(data);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mDataList.size())
            return null;
        return mDataList.get(position);
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
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsEntity item = mDataList.get(position);
        holder.title.setText(item.title);

        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView image;
    }
}
