package com.rainsong.toutiao.data;

import com.rainsong.toutiao.bean.NewsListBean;
import com.rainsong.toutiao.entity.ArticleListResponseEntity;
import com.rainsong.toutiao.entity.GroupInfoEntity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

import static java.lang.System.currentTimeMillis;

/**
 * Created by maxliaops on 17-3-17.
 */

public class DataManager {

    private static DataManager sInstance;
    private Retrofit retrofit;
    private JuheService mJuheService;
    private TouTiaoService mTouTiaoService;

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public DataManager() {
        mJuheService = JuheService.Creator.newJuheService();
        mTouTiaoService = TouTiaoService.Creator.newTouTiaoService();
    }

    public Observable<NewsListBean> getNews(String type) {
        return mJuheService.getNews(JuheService.JUHE_APPKEY, type);
    }

    public Observable<ArticleListResponseEntity> getFeedArticleList(String category) {
        return mTouTiaoService.getArticleList(TouTiaoService.URL_NEWS_FEED,
                getArticleListQueryMap(category));
    }

    public static Map<String, String> getArticleListQueryMap(String category) {
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("category", category);
        queryMap.put("refer", "1");
        queryMap.put("count", "20");
        queryMap.put("tt_from", "enter_auto");
        queryMap.put("lac", "9763");
        queryMap.put("cid", "46794088");
        queryMap.put("cp", "5981d405e60fbq1");
        queryMap.put("iid", "6106322388");
        queryMap.put("device_id", "15517010658");
        queryMap.put("ac", "wifi");
        queryMap.put("channel", "xiaomi");
        queryMap.put("app_name", "news_article");
        queryMap.put("version_code", "584");
        queryMap.put("version_name", "5.8.4");
        queryMap.put("device_platform", "android");
        queryMap.put("ssmix", "a");
        queryMap.put("device_type", "Redmi+3");
        queryMap.put("device_brand", "Xiaomi");
        queryMap.put("language", "zh");
        queryMap.put("os_api", "22");
        queryMap.put("os_version", "5.1.1");
        queryMap.put("uuid", "860635039439759");
        queryMap.put("openudid", "327f9a52884952a8");
        queryMap.put("manifest_version_code", "584");
        queryMap.put("resolution", "720*1280");
        queryMap.put("dpi", "320");
        queryMap.put("update_version_code", "5844");

        long timestamp = System.currentTimeMillis();
        queryMap.put("_rticket", String.valueOf(timestamp));

        return queryMap;
    }

}
