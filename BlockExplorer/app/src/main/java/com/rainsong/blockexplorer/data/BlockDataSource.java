package com.rainsong.blockexplorer.data;

import android.support.annotation.NonNull;

import rx.Observable;

public interface BlockDataSource {
    Observable<Block> getBlock(@NonNull String blockDate);

    void saveBlock(@NonNull Block block);
}
