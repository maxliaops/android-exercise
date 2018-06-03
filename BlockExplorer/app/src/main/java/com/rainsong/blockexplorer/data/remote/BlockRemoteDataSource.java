package com.rainsong.blockexplorer.data.remote;

import android.support.annotation.NonNull;

import com.rainsong.blockexplorer.data.Block;
import com.rainsong.blockexplorer.data.BlockDataSource;

import rx.Observable;
import rx.functions.Func1;

public class BlockRemoteDataSource implements BlockDataSource {
    private static BlockRemoteDataSource INSTANCE;

    private BlockExplorerApi mBlockExplorerApi;

    public static BlockRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private BlockRemoteDataSource() {
        mBlockExplorerApi = RetrofitService.getBlockExplorerApi();
    }

    @Override
    public Observable<Block> getBlock(@NonNull final String blockDate) {
        return mBlockExplorerApi.getBlocks(blockDate)
                .flatMap(new Func1<String, Observable<Block>>() {
                    @Override
                    public Observable<Block> call(String content) {
                        Block block = new Block();
                        block.setBlockDate(blockDate);
                        block.setContent(content);
                        return Observable.just(block);
                    }
                });
    }

    @Override
    public void saveBlock(@NonNull Block block) {

    }
}
