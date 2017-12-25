package com.rainsong.hireme.data;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.rainsong.hireme.bean.HireFragmentInfo;
import com.rainsong.hireme.bean.JobberDetailInfo;
import com.rainsong.hireme.bean.JobbersListInfo;
import com.rainsong.hireme.bean.WechatInfo;
import com.rainsong.hireme.util.CommonUtils;
import com.rainsong.hireme.util.GsonUtil;
import com.rainsong.hireme.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-20.
 */

public class DataManager {
    private static final String FILE_HIRE_MEMBERS_ASSET = "hire_members.json";

    private static DataManager sInstance;
    private HireMeService mHireMeService;

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public DataManager() {
        mHireMeService = HireMeService.Creator.newHireMeService();
    }

    public Observable<HireFragmentInfo> getMembers(final Context context) {
        return Observable.create(new Observable.OnSubscribe<HireFragmentInfo>() {
            @Override
            public void call(Subscriber<? super HireFragmentInfo> subscriber) {
                Timber.d("getMembers");
                HireFragmentInfo info = getHireFragmentInfoFromAsset(context);
                subscriber.onNext(info);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<JobbersListInfo> getJobbersListInfo(String freshId, int start, boolean loading) {
        Map paramMap = new HashMap();
        paramMap.put("typeby", 11);
        paramMap.put("fresh_id", freshId);
        paramMap.put("start", start);
        paramMap.put("city", "深圳");
        if(loading) {
            paramMap.put("loading", "true");
        } else {
            paramMap.put("loading", "false");
        }
        paramMap.put("sex", "2");
        paramMap.put("age_1", "18");
        paramMap.put("age_2", "70");
        paramMap.put("height_1", "150");
        paramMap.put("height_2", "220");
        paramMap.put("price", "0");
        paramMap.put("rand", "0");
        paramMap.put("identify", "0");
        String toJson = GsonUtil.getGson().toJson(paramMap);
        String str = new String(Base64.encode(toJson.getBytes(), 0));
        Timber.d("fresh_id: " + freshId + ", start: " + start + ", str=" + str);
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("c", "Jobbers");
        queryMap.put("m", "listJobbers");
        queryMap.put("p", str);
        return mHireMeService.getJobbersListInfo(queryMap);
    }

    public Observable<JobberDetailInfo> getJobberDetailInfo(String userId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("lo", "113.944871");
        paramMap.put("la", "22.548634");
        paramMap.put("to_user_id", userId);
        paramMap.put("be_user_id", "617583");
        String toJson = GsonUtil.getGson().toJson(paramMap);
        String str = new String(Base64.encode(toJson.getBytes(), 0));
        Timber.d("user_id: " + userId + ", str=" + str);
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("c", "Bomb");
        queryMap.put("m", "detail");
        queryMap.put("p", str);
        return mHireMeService.getJobberDetailInfo(queryMap);
    }

    public Observable<WechatInfo> getJobberWechatInfo(String userId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("to_user_id", userId);
        paramMap.put("be_user_id", userId);
        String toJson = GsonUtil.getGson().toJson(paramMap);
        String str = new String(Base64.encode(toJson.getBytes(), 0));
        Timber.d("user_id: " + userId + ", str=" + str);
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("c", "LittleSet");
        queryMap.put("m", "getWechat");
        queryMap.put("p", str);
        return mHireMeService.getJobberWechatInfo(queryMap);
    }

    private HireFragmentInfo getHireFragmentInfoFromAsset(Context context) {
        String strFromAsset = CommonUtils.getStringFromAsset(context, FILE_HIRE_MEMBERS_ASSET);
        if (StringUtils.isEmpty(strFromAsset)) {
            return null;
        }
        return GsonUtil.getGson().fromJson(strFromAsset,
                HireFragmentInfo.class);
    }
}
