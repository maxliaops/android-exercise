package com.rainsong.tiantiannews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainsong.tiantiannews.R;
import com.rainsong.tiantiannews.bean.NewsListBean.ResultBean.DataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maxliaops on 17-1-13.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<DataBean> mDataList;
    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context, List<DataBean> list) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataList = list == null ? new ArrayList<DataBean>()
                : new ArrayList<DataBean>(list);
    }

    public void updateData(List<DataBean> list) {
        this.mDataList = list == null ? new ArrayList<DataBean>()
                : new ArrayList<DataBean>(list);
        this.notifyDataSetChanged();
    }

    public DataBean getItemData(int position) {
        if (mDataList != null) {
            return mDataList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsItemViewHolder(mInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NewsItemViewHolder) {
            DataBean item = mDataList.get(position);
            NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder) holder;
            newsItemViewHolder.newsTitleView.setText(item.getTitle());
            newsItemViewHolder.newsAuthorName.setText(item.getAuthorName());
            newsItemViewHolder.newsDate.setText(item.getDate());

            Glide.with(mContext)
                    .load(item.getThumbnail_pic_s())
                    .into(newsItemViewHolder.newsImageView);
            newsItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_NORMAL;
    }

    public static class NewsItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_news_title)
        TextView newsTitleView;
        @BindView(R.id.tv_news_author_name)
        TextView newsAuthorName;
        @BindView(R.id.tv_news_date)
        TextView newsDate;
        @BindView(R.id.iv_news_pic)
        ImageView newsImageView;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
