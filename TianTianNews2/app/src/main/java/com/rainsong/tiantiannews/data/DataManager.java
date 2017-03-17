package com.rainsong.tiantiannews.data;

import com.rainsong.tiantiannews.bean.NewsListBean;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by maxliaops on 17-3-17.
 */

public class DataManager {

    private static DataManager sInstance;
    private Retrofit retrofit;
    private JuheService mJuheService;

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public DataManager() {
        mJuheService = JuheService.Creator.newJuheService();
    }

    public Observable<NewsListBean> getNews(String type) {
        return mJuheService.getNews(JuheService.JUHE_APPKEY, type);
    }
}
