package com.swipefinish;

import android.app.Application;

import com.swipefinish.helper.ActivityLifeHelper;

/**
 * Created by yongfeng on 2016/11/26.
 * Email:2499522170@qq.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(ActivityLifeHelper.instance());
    }
}
