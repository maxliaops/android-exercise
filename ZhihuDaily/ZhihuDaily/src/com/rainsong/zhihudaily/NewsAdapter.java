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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rainsong.zhihudaily.NewsListEntity.NewsEntity;

public class NewsAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<NewsEntity> mDataList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public NewsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataList = new ArrayList<NewsEntity>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_small_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.image_small_default) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.image_small_default) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成

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
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsEntity item = mDataList.get(position);
        holder.title.setText(item.title);
        mImageLoader.displayImage(item.images.get(0), holder.image, mOptions);

        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView image;
    }
}
