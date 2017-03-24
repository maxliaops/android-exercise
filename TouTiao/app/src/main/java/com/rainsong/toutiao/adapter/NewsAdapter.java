package com.rainsong.toutiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by maxliaops on 17-1-13.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NO_IMAGE = 0;
    private static final int ITEM_TYPE_SMALL_IMAGE = 1;
    private static final int ITEM_TYPE_MULTI_IMAGE = 2;

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

    public List<GroupInfoEntity> getData() {
        return mDataList;
    }

    public void updateData(List<GroupInfoEntity> list) {
        mDataList = list == null ? new ArrayList<GroupInfoEntity>()
                : new ArrayList<GroupInfoEntity>(list);
        notifyDataSetChanged();
    }

    public void addData(List<GroupInfoEntity> list) {
        if (mDataList != null) {
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
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
        if (viewType == ITEM_TYPE_SMALL_IMAGE) {
            return new NewsItemSmallImageViewHolder(mInflater.inflate(R.layout
                    .item_news_small_image, parent, false));
        } else if (viewType == ITEM_TYPE_MULTI_IMAGE) {
            return new NewsItemMultiImageViewHolder(mInflater.inflate(R.layout
                    .item_news_multi_image, parent, false));
        } else {
            return new NewsItemNoImageViewHolder(mInflater.inflate(R.layout.item_news_no_image,
                    parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NewsItemSmallImageViewHolder) {
            GroupInfoEntity item = mDataList.get(position);
            NewsItemSmallImageViewHolder newsItemSmallImageViewHolder =
                    (NewsItemSmallImageViewHolder) holder;
            newsItemSmallImageViewHolder.newsTitleView.setText(item.getTitle());
            newsItemSmallImageViewHolder.newsAuthorName.setText(item.getSource());
            String date = timestamp2Date(String.valueOf(item.getPublish_time()), null);
            newsItemSmallImageViewHolder.newsDate.setText(date);

            Glide.with(mContext)
                    .load(item.getMiddle_image().getUrl())
                    .into(newsItemSmallImageViewHolder.newsImageView);

            newsItemSmallImageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        } else if (holder instanceof NewsItemMultiImageViewHolder) {
            GroupInfoEntity item = mDataList.get(position);
            NewsItemMultiImageViewHolder newsItemMultiImageViewHolder =
                    (NewsItemMultiImageViewHolder) holder;
            newsItemMultiImageViewHolder.newsTitleView.setText(item.getTitle());
            newsItemMultiImageViewHolder.newsAuthorName.setText(item.getSource());
            String date = timestamp2Date(String.valueOf(item.getPublish_time()), null);
            newsItemMultiImageViewHolder.newsDate.setText(date);

            String url0 = item.getImage_list().get(0).getUrl();
            String url1 = item.getImage_list().get(1).getUrl();
            String url2 = item.getImage_list().get(2).getUrl();
//            Log.d(TAG, "NewsItemMultiImageViewHolder " + item.getTitle() + " " + url0 + " " + url1 + " " + url2);
            Glide.with(mContext)
                    .load(item.getImage_list().get(0).getUrl())
                    .into(newsItemMultiImageViewHolder.newsImageView0);
            Glide.with(mContext)
                    .load(item.getImage_list().get(1).getUrl())
                    .into(newsItemMultiImageViewHolder.newsImageView1);
            Glide.with(mContext)
                    .load(item.getImage_list().get(2).getUrl())
                    .into(newsItemMultiImageViewHolder.newsImageView2);

            newsItemMultiImageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        } else if (holder instanceof NewsItemNoImageViewHolder) {
            GroupInfoEntity item = mDataList.get(position);
            NewsItemNoImageViewHolder newsItemNoImageViewHolder = (NewsItemNoImageViewHolder)
                    holder;
            newsItemNoImageViewHolder.newsTitleView.setText(item.getTitle());
            newsItemNoImageViewHolder.newsAuthorName.setText(item.getSource());
            String date = timestamp2Date(String.valueOf(item.getPublish_time()), null);
            newsItemNoImageViewHolder.newsDate.setText(date);

            newsItemNoImageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        GroupInfoEntity groupInfoEntity = mDataList.get(position);
        if (groupInfoEntity.isHas_image()) {
            if (groupInfoEntity.getImage_list() != null && groupInfoEntity.getImage_list().size()
                    >= 3) {
                return ITEM_TYPE_MULTI_IMAGE;
            } else if (groupInfoEntity.getMiddle_image() != null) {
                return ITEM_TYPE_SMALL_IMAGE;
            } else {
                return ITEM_TYPE_NO_IMAGE;
            }
        } else {
            return ITEM_TYPE_NO_IMAGE;
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timestamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static class NewsItemNoImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_news_title)
        TextView newsTitleView;
        @BindView(R.id.tv_news_author_name)
        TextView newsAuthorName;
        @BindView(R.id.tv_news_date)
        TextView newsDate;

        public NewsItemNoImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NewsItemSmallImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_news_title)
        TextView newsTitleView;
        @BindView(R.id.tv_news_author_name)
        TextView newsAuthorName;
        @BindView(R.id.tv_news_date)
        TextView newsDate;
        @BindView(R.id.iv_news_pic)
        ImageView newsImageView;

        public NewsItemSmallImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NewsItemMultiImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_news_title)
        TextView newsTitleView;
        @BindView(R.id.tv_news_author_name)
        TextView newsAuthorName;
        @BindView(R.id.tv_news_date)
        TextView newsDate;
        @BindView(R.id.iv_news_pic0)
        ImageView newsImageView0;
        @BindView(R.id.iv_news_pic1)
        ImageView newsImageView1;
        @BindView(R.id.iv_news_pic2)
        ImageView newsImageView2;

        public NewsItemMultiImageViewHolder(View itemView) {
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
