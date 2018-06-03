package com.rainsong.blockexplorer.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by maxliaops on 17-12-20.
 */

public class RetrofitService {
    private static int DEFAULT_TIMEOUT = 1500;
    private static String BASE_URL = "https://blockexplorer.com/api/";

    private static BlockExplorerApi mBlockExplorerApi;

    public static BlockExplorerApi getBlockExplorerApi() {
        if(mBlockExplorerApi == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(StringConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mBlockExplorerApi = retrofit.create(BlockExplorerApi.class);
        }
        return mBlockExplorerApi;
    }
}
