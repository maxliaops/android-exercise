package com.rainsong.blockexplorer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rainsong.blockexplorer.R;
import com.rainsong.blockexplorer.adapter.BlockListAdapter;
import com.rainsong.blockexplorer.base.BaseFragment;
import com.rainsong.blockexplorer.bean.BlockListInfo;
import com.rainsong.blockexplorer.data.Block;
import com.rainsong.blockexplorer.data.BlockRepository;
import com.rainsong.blockexplorer.data.Injection;
import com.rainsong.blockexplorer.util.GsonUtil;
import com.rainsong.blockexplorer.util.SHA3Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-20.
 */

public class BlockFragment extends BaseFragment implements BlockListAdapter.OnItemClickListener,
        View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_current_date)
    EditText mETCurrentDate;

    @BindView(R.id.btn_prev)
    Button mBtnPrev;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_stop)
    Button mBtnStop;

    private Context mContext;
    private BlockRepository mBlockRepository;
    private BlockListAdapter mAdapter;
    private Calendar mCalendar;
    private SimpleDateFormat mSDF;
    private String mCurrentDate;
    private boolean mStopFlag = false;
    private int mRetryCount = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mBlockRepository = Injection.provideBlockRepository(mContext);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mSDF = new SimpleDateFormat("yyyy-MM-dd");
        mSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_block, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new BlockListAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mBtnPrev.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mCurrentDate = mSDF.format(mCalendar.getTime());
        mETCurrentDate.setText(mCurrentDate);
//        getBlockInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getBlockInfo() {
        mCurrentDate = mSDF.format(mCalendar.getTime());
        if("2009-01-09".equalsIgnoreCase(mCurrentDate)) {
            return;
        }
        mETCurrentDate.setText(mCurrentDate);
        mBlockRepository.getBlock(mCurrentDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Block>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()" + mCurrentDate);
                        if(!mStopFlag) {
                            mCalendar.add(Calendar.DATE, -1);
                            mRetryCount = 1;
                            getBlockInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAdapter.setData(null);
                        e.printStackTrace();
                        Timber.d("onError(): " + mCurrentDate);
                        if(!mStopFlag) {
                            if(mRetryCount > 0) {
                                mRetryCount--;
                            } else {
                                mRetryCount = 1;
                                mCalendar.add(Calendar.DATE, -1);
                            }
                            getBlockInfo();
                        }
                    }

                    @Override
                    public void onNext(Block block) {
                        if(block != null) {
                            String blockDate = block.getBlockDate();
                            String content = block.getContent();
                            Timber.d("blockDate=" + blockDate);
                            BlockListInfo blockListInfo = GsonUtil.getGson().fromJson(content, BlockListInfo.class);
                            if(blockListInfo != null) {
                                BlockListInfo.Pagination pagination = blockListInfo.getPagination();
                                if (pagination != null) {
//                                    mBtnPrev.setText(pagination.getPrev());
//                                    mBtnNext.setText(pagination.getNext());
                                }
                                List<BlockListInfo.BlockInfo> blocks = blockListInfo.getBlocks();
                                if (blocks != null && blocks.size() > 0) {
                                    mAdapter.setData(blocks);
                                } else {
                                    mAdapter.setData(null);
                                }
                            }
                        }
                    }
                });
    }
    private static long bytesToInt(byte a, byte b, byte c, byte d) {
        return (a & 0xFF |
                (b & 0xFF) << 8 |
                (c & 0xFF) << 16 |
                (d & 0xFF) << 24) & 0xFFFFFFFFL;
    }

    private List<Integer> getWinNumbers(String blockHash, int numbersCount, int numbersCountMax) {
        byte[] sha3 = SHA3Helper.getSha3Hash(blockHash);
//        Timber.d("sha3 = " + Hex.toHexString(sha3));
        int[] allNumbers = new int[numbersCountMax];
        List<Integer> winNumbers = new ArrayList<>();
//        int[] winNumbers = new int[numbersCount];
        for (int i = 0; i < numbersCountMax; i++) {
            allNumbers[i] = i + 1;
        }
        for (int i = 0; i < numbersCount; i++) {
            int n = numbersCountMax - i;
            long k = bytesToInt(sha3[4 * i], sha3[4 * i + 1], sha3[4 * i + 2], sha3[4 * i + 3]);
            int r = (int) (k % n);
//            Timber.d("n = " + n + " k = " + k + " r = " + r);
//            winNumbers[i] = allNumbers[r];
            winNumbers.add(allNumbers[r]);
            allNumbers[r] = allNumbers[n - 1];
        }
        return winNumbers;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev:
                mCalendar.add(Calendar.DATE, -1);
                mStopFlag = false;
                mRetryCount = 1;
                getBlockInfo();
                break;
            case R.id.btn_next:
                mCalendar.add(Calendar.DATE, 1);
                mStopFlag = false;
                mRetryCount = 1;
                getBlockInfo();
                break;
            case R.id.btn_start:
                try {
                    mCurrentDate = String.valueOf(mETCurrentDate.getText());
                    Timber.d("start " + mCurrentDate);
                    Date date = mSDF.parse(mCurrentDate);
                    mCalendar.setTime(date);
                    mStopFlag = false;
                    mRetryCount = 1;
                    getBlockInfo();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_stop:
                mStopFlag = true;
                break;
        }
    }
}
