package com.rainsong.toutiao.data;

import com.rainsong.toutiao.entity.ArticleListResponseEntity;

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

import static com.rainsong.toutiao.data.JuheService.DEFAULT_TIMEOUT;

/**
 * Created by maxliaops on 17-3-23.
 */

public interface TouTiaoService {
    int DEFAULT_TIMEOUT = 5;
    String BASE_URL = "https://iu.snssdk.com";
    String URL_NEWS_FEED = "https://iu.snssdk.com/api/news/feed/v47/";

    @GET
    Observable<ArticleListResponseEntity> getArticleList(@Url String url, @QueryMap Map<String, String> queryMap);

    class Creator {
        public static TouTiaoService newTouTiaoService() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TouTiaoService.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(TouTiaoService.class);
        }
    }
}
