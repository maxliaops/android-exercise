package com.rainsong.toutiao.data;

import com.rainsong.toutiao.bean.NewsListBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by maxliaops on 17-3-17.
 */

public interface JuheService {
    String JUHE_APPKEY = "4e1d849f4ee325ef117b324d2c834ff2";
    int DEFAULT_TIMEOUT = 5;
    String ENDPOINT = "http://v.juhe.cn/";

    @GET("toutiao/index")
    Observable<NewsListBean> getNews(@Query("key") String key, @Query("type") String type);

    class Creator {
        public static JuheService newJuheService() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JuheService.ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(JuheService.class);
        }
    }
}
