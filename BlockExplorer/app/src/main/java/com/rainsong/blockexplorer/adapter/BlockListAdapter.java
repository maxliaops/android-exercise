package com.rainsong.blockexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainsong.blockexplorer.R;
import com.rainsong.blockexplorer.bean.BlockListInfo.BlockInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class BlockListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BlockInfo> mList;
    private SimpleDateFormat mSDF;
    private OnItemClickListener mOnItemClickListener;


    public BlockListAdapter(Context context) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
        mSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void setData(List<BlockInfo> blocks) {
        if (mList != null) {
            mList.clear();
            if(blocks != null) {
                Timber.d("setData() size=" + blocks.size());
                mList.addAll(blocks);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public BlockInfo getItemData(int position) {
        if (mList != null) {
            return mList.get(position);
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
        if (mList == null) return 0;
        int count = mList.size();
//        Timber.d("getItemCount(): " + count);
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Timber.d("onCreateViewHolder(): viewType=" + viewType);
        if (viewType == ITEM_TYPE_NORMAL) {
            View itemView = mInflater.inflate(R.layout.item_block, parent, false);
            return new BlockViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        Timber.d("onBindViewHolder(): position=" + position);
        if (holder instanceof BlockViewHolder) {
            BlockInfo item = mList.get(position);
            BlockViewHolder viewHolder = (BlockViewHolder) holder;
            int blockIndex = item.getHeight();
            long blockTimestamp = item.getTime();
            int transactions = item.getTxlength();
            int size = item.getSize();
            BlockInfo.PoolInfo poolInfo = item.getPoolInfo();
            if(poolInfo != null) {
                String miner = poolInfo.getPoolName();
                viewHolder.miner.setText(miner);
            }
            Date date = new Date(blockTimestamp * 1000);
            viewHolder.index.setText(String.valueOf(blockIndex));
            viewHolder.size.setText(String.valueOf(size));
            viewHolder.transactions.setText(String.valueOf(transactions));
            viewHolder.timestamp.setText(mSDF.format(date));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public static class BlockViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.block_index)
        TextView index;
        @BindView(R.id.block_timestamp)
        TextView timestamp;
        @BindView(R.id.block_transactions)
        TextView transactions;
        @BindView(R.id.block_miner)
        TextView miner;
        @BindView(R.id.block_size)
        TextView size;

        public BlockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
