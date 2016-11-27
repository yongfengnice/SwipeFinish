package com.swipefinish.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by yongfeng on 2016/11/26.
 * Email:2499522170@qq.com
 * 空实现
 */
public class BaseActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
