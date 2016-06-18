package com.rainsong.zhihudaily.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hiremeplz.hireme.bean.DetailsInfo;
import com.hiremeplz.hireme.bean.DetailsInfo.MsgEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rainsong.zhihudaily.R;
import com.rainsong.zhihudaily.util.Rsa;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class HireDetailsActivity extends Activity {
    private static final String TAG = "HireDetailsActivity";

    public static final String SERVER_URL = "http://123.56.205.35:2345";

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private TextView tv_mobile;
    private ImageView iv_image;
    private String jobber;
    private String jobber_id;
    private DetailsInfo detailsInfo;
    private MsgEntity msgEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_detail);
        Intent intent = getIntent();
        this.jobber_id = intent.getStringExtra("jobber_id");
        this.jobber = intent.getStringExtra("jobber");
        Log.d(TAG, "jobber: " + this.jobber);
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_small_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.image_small_default) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.image_small_default) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成
        initView();
        initData();
    }

    private void initView() {
        tv_mobile = (TextView)findViewById(R.id.mobile);
        iv_image = (ImageView)findViewById(R.id.image);
    }

    private void initData() {
        Map hashMap = new HashMap();
        hashMap.put("jobber_id",
                Integer.valueOf(Integer.parseInt(jobber_id)));
        HashMap hashMap2 = new HashMap();
        hashMap2.put("c", "Jobbers");
        hashMap2.put("m", "detail");
        hashMap2.put("p", hashMap);
        String toJson = new Gson().toJson(hashMap2);
        Log.d(TAG, "initData: " + toJson);
        toJson = Rsa.encryptByPublic(toJson);
        Log.d(TAG, "initData--str: " + toJson);
        OkHttpUtils.postString().url(SERVER_URL)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(toJson).build().execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        public MyStringCallback() {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d(TAG, "onError: " + e);
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d(TAG, "onResponse: " + response);

            try {
                JSONObject jSONObject = new JSONObject(response);
                String code = jSONObject.get("code").toString();
                if (code.equals("0")) {
                    detailsInfo = (DetailsInfo) new Gson().fromJson(response, DetailsInfo.class);
                    msgEntity = detailsInfo.getData().getMsg().get(0);
                    tv_mobile.setText(msgEntity.getMobile());
                    String imageUrl = "http://img3.hiremeplz.com/" + msgEntity.getQrcode();
                    mImageLoader.displayImage(imageUrl, iv_image,
                            mOptions);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
