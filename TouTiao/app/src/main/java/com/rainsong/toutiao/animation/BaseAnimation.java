package com.rainsong.toutiao.animation;

/**
 * Created by maxliaops on 17-3-24.
 */

import android.animation.Animator;
import android.view.View;

/**
 * 基本动画
 */
public interface  BaseAnimation {

    Animator[] getAnimators(View view);

}
