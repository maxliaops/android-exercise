package com.rainsong.blockexplorer.data.remote;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by maxliaops on 17-12-21.
 */

public interface BlockExplorerApi {
    @GET("blocks")
    Observable<String> getBlocks(@Query("blockDate") String blockDate);

}
