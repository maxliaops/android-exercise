package com.rainsong.blockexplorer.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.NoSuchElementException;
import java.util.Timer;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import timber.log.Timber;

import static android.support.v4.util.Preconditions.checkNotNull;

public class BlockRepository implements BlockDataSource {
    @Nullable
    private static BlockRepository INSTANCE = null;

    @NonNull
    private final BlockDataSource mBlockLocalDataSource;
    @NonNull
    private final BlockDataSource mBlockRemoteDataSource;

    private BlockRepository(@NonNull BlockDataSource blockLocalDataSource,
                            @NonNull BlockDataSource blockRemoteDataSource) {
        mBlockLocalDataSource = blockLocalDataSource;
        mBlockRemoteDataSource = blockRemoteDataSource;
    }

    public static BlockRepository getInstance(@NonNull BlockDataSource blockLocalDataSource,
                                              @NonNull BlockDataSource blockRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BlockRepository(blockLocalDataSource, blockRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<Block> getBlock(@NonNull String blockDate) {
        checkNotNull(blockDate);

        Observable<Block> localBlock = getLocalBlock(blockDate);
        Observable<Block> remoteBlock = getRemoteBlock(blockDate);
        return Observable.concat(localBlock, remoteBlock).first()
                .doOnNext(new Action1<Block>() {
                    @Override
                    public void call(Block block) {
                        if(block == null) {
                            Timber.d("block null");
                        } else {
                            Timber.d("block " + block.getBlockDate());
                        }
                    }
                })
                .map(block -> {
                    if(block == null) {
                        throw new NoSuchElementException("No block found with blockDate " + blockDate);
                    }
                    return block;
                });
    }


    @Override
    public void saveBlock(@NonNull Block block) {
        checkNotNull(block);
        mBlockLocalDataSource.saveBlock(block);
    }

    @NonNull
    Observable<Block> getLocalBlock(@NonNull final String blockDate) {
        return mBlockLocalDataSource
                .getBlock(blockDate)
                .doOnNext(block -> {
                    Timber.d("getLocalBlock block=" + block);
                })
                .first()
                .filter(block -> block != null);
    }

    @NonNull
    Observable<Block> getRemoteBlock(@NonNull final String blockDate) {
        return mBlockRemoteDataSource
                .getBlock(blockDate)
                .doOnNext(block -> {
                    Timber.d("getRemoteBlock block=" + block);
                    mBlockLocalDataSource.saveBlock(block);
                });
    }
}
