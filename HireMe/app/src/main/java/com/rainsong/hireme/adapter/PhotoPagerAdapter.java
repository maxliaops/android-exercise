package com.rainsong.hireme.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-21.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mPhotoUrls;

    public PhotoPagerAdapter(Context mContext) {
        this.mContext = mContext;
        this.mPhotoUrls = new ArrayList<>();
    }

    public void addData(List<String> photoUrls) {
        if(mPhotoUrls != null) {
            mPhotoUrls.addAll(photoUrls);
        }
        notifyDataSetChanged();
    }

    public void addData(String photoUrl) {
        if(mPhotoUrls != null) {
            mPhotoUrls.add(0, photoUrl);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mPhotoUrls != null) {
            return mPhotoUrls.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        String photoUrl = mPhotoUrls.get(position);
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        Timber.d("instantiateItem(): photoUrl=" + photoUrl);
        Glide.with(mContext)
                .load(photoUrl)
                .into(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
