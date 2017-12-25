package com.rainsong.hireme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainsong.hireme.R;
import com.rainsong.hireme.bean.JobbersListInfo.DataEntity.RetDataEntity;
import com.rainsong.hireme.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.R.attr.name;

/**
 * Created by maxliaops on 17-12-20.
 */

public class JobbersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RetDataEntity> mMembers;
    private OnItemClickListener mOnItemClickListener;


    public JobbersListAdapter(Context context) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMembers = new ArrayList<>();
    }

    public void addData(List<RetDataEntity> members) {
        if(mMembers != null) {
            Timber.d("addData() size=" + members.size());
            mMembers.addAll(members);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public RetDataEntity getItemData(int position) {
        if (mMembers != null) {
            return mMembers.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
//        Timber.d("getItemViewType(): position=" + position);
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mMembers == null) return 0;
        int count = mMembers.size();
//        Timber.d("getItemCount(): " + count);
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Timber.d("onCreateViewHolder(): viewType=" + viewType);
        if (viewType == ITEM_TYPE_NORMAL) {
            View itemView = mInflater.inflate(R.layout.griditem_group_member, parent, false);
            return new JobberViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        Timber.d("onBindViewHolder(): position=" + position);
        if (holder instanceof JobberViewHolder) {
            RetDataEntity item = mMembers.get(position);
            JobberViewHolder jobberViewHolder = (JobberViewHolder) holder;
            String rentNum = item.getRent_num();
            String name = rentNum + "次_" + item.getName();
            jobberViewHolder.jobberName.setText(name);
            String headImageUrl = item.getPhoto_1();
            if (!StringUtils.isEmpty(headImageUrl)) {
                Glide.with(mContext)
                        .load(headImageUrl)
                        .into(jobberViewHolder.jobberHeadImage);
            } else {
                jobberViewHolder.jobberHeadImage.setImageResource(R.mipmap.moren_g);
            }
            jobberViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public static class JobberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_text)
        TextView jobberName;
        @BindView(R.id.head_image)
        ImageView jobberHeadImage;

        public JobberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
