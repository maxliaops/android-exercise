package com.rainsong.blockexplorer.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rainsong.blockexplorer.data.local.BlockLocalDataSource;
import com.rainsong.blockexplorer.data.remote.BlockRemoteDataSource;

import static android.support.v4.util.Preconditions.checkNotNull;

public class Injection {

    public static BlockRepository provideBlockRepository(@NonNull Context context) {
        checkNotNull(context);
        return BlockRepository.getInstance(BlockLocalDataSource.getInstance(context),
                BlockRemoteDataSource.getInstance());
    }
}
