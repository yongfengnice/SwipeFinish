package com.swipefinish.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yongfeng on 2016/11/26.
 * Email:2499522170@qq.com
 */
public class WindowHelper {
    private static WindowHelper sWindowHelper;
    private int mScreenWidth;

    public static WindowHelper instance() {
        if (sWindowHelper == null) {
            synchronized (WindowHelper.class) {
                if (sWindowHelper == null) {
                    sWindowHelper = new WindowHelper();
                }
            }
        }
        return sWindowHelper;
    }

    public ViewGroup getDecorView(Activity activity) {
        if (activity == null) {
            return null;
        }
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    public View getContentView(Activity activity) {
        ViewGroup decorView = getDecorView(activity);
        if (decorView == null) {
            return null;
        }
        return decorView.getChildAt(0);
    }

    //获取屏幕宽度
    public int getScreenWidth(Context context) {
        if (mScreenWidth == 0) {
            mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }
}
