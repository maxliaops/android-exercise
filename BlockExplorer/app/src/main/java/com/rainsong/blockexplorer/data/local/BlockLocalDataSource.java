package com.rainsong.blockexplorer.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rainsong.blockexplorer.app.App;
import com.rainsong.blockexplorer.data.Block;
import com.rainsong.blockexplorer.data.BlockDao;
import com.rainsong.blockexplorer.data.BlockDataSource;
import com.rainsong.blockexplorer.data.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.support.v4.util.Preconditions.checkNotNull;

public class BlockLocalDataSource implements BlockDataSource {
    @Nullable
    private static BlockLocalDataSource INSTANCE;

    private BlockDao mBlockDao;
    private RxDao<Block, Long> rxDao;
    private RxQuery<Block> blockQuery;

    // Prevent direct instantiation.
    private BlockLocalDataSource(@NonNull Context context) {
        checkNotNull(context, "context cannot be null");
        // get the Rx variant of the note DAO
        DaoSession daoSession = App.getDaoSession();
        mBlockDao = daoSession.getBlockDao();

        // query all notes, sorted a-z by their text
        blockQuery = daoSession.getBlockDao().queryBuilder().orderAsc(BlockDao.Properties.BlockDate).rx();
    }

    public static BlockLocalDataSource getInstance(
            @NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BlockLocalDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<Block> getBlock(@NonNull String blockDate) {
        QueryBuilder<Block> queryBuilder = mBlockDao.queryBuilder().where(BlockDao.Properties.BlockDate.eq(blockDate));
        RxQuery<Block> rxQuery = queryBuilder.rx();
        return rxQuery.unique();
    }

    @Override
    public void saveBlock(@NonNull Block block) {
        mBlockDao.rx()
                .insert(block)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Block>() {
                    @Override
                    public void call(Block block) {
                        Timber.d("Inserted new Block: " + block.getBlockDate());
                    }
                });
    }
}
