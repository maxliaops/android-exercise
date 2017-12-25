package com.rainsong.hireme.data;

import com.rainsong.hireme.bean.HireFragmentInfo;
import com.rainsong.hireme.bean.JobberDetailInfo;
import com.rainsong.hireme.bean.JobbersListInfo;
import com.rainsong.hireme.bean.WechatInfo;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by maxliaops on 17-12-21.
 */

public interface HireMeService {
    int DEFAULT_TIMEOUT = 5;
    String BASE_URL = "http://123.56.205.35:2354/";

    @GET("/")
    Observable<JobbersListInfo> getJobbersListInfo(@QueryMap Map<String, String> queryMap);

    @GET("/")
    Observable<JobberDetailInfo> getJobberDetailInfo(@QueryMap Map<String, String> queryMap);

    @GET("/")
    Observable<WechatInfo> getJobberWechatInfo(@QueryMap Map<String, String> queryMap);

    class Creator {
        public static HireMeService newHireMeService() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(HireMeService.class);
        }
    }
}
