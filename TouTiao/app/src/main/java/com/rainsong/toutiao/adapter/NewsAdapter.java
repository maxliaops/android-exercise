package com.rainsong.toutiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainsong.toutiao.R;
import com.rainsong.toutiao.entity.GroupInfoEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    protected List<GroupInfoEntity> mDataList;
    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context, List<GroupInfoEntity> list) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataList = list == null ? new ArrayList<GroupInfoEntity>()
                : new ArrayList<GroupInfoEntity>(list);
    }

    public void updateData(List<GroupInfoEntity> list) {
        this.mDataList = list == null ? new ArrayList<GroupInfoEntity>()
                : new ArrayList<GroupInfoEntity>(list);
        this.notifyDataSetChanged();
    }

    public GroupInfoEntity getItemData(int position) {
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
            GroupInfoEntity item = mDataList.get(position);
            NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder) holder;
            newsItemViewHolder.newsTitleView.setText(item.getTitle());
            newsItemViewHolder.newsAuthorName.setText(item.getSource());
            String date = timestamp2Date(String.valueOf(item.getPublish_time()), null);
            newsItemViewHolder.newsDate.setText(date);

            if(item.getMiddle_image() != null) {
                Glide.with(mContext)
                        .load(item.getMiddle_image().getUrl())
                        .into(newsItemViewHolder.newsImageView);
            } else if(item.getImage_list() != null) {
                Glide.with(mContext)
                        .load(item.getImage_list().get(0).getUrl())
                        .into(newsItemViewHolder.newsImageView);
            }
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

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timestamp2Date(String seconds, String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
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
