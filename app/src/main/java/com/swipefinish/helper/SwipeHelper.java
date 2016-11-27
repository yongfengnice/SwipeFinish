package com.swipefinish.helper;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yongfeng on 2016/11/27.
 * Email:2499522170@qq.com
 */
public class SwipeHelper {
    private static SwipeHelper sSwipeHelper;

    private float mDownX;
    private float mMoveX;
    private float mMaxMoveX;
    private long mDownTime;
    private int mSlideX = 30;

    public static SwipeHelper instance() {
        if (sSwipeHelper == null) {
            synchronized (WindowHelper.class) {
                if (sSwipeHelper == null) {
                    sSwipeHelper = new SwipeHelper();
                }
            }
        }
        return sSwipeHelper;
    }

    public void processTouchEvent(MotionEvent ev) {
        Activity curActivity = ActivityLifeHelper.instance().getCurrentActivity();
        Activity preActivity = ActivityLifeHelper.instance().getPreviewActivity();
        if (curActivity == null || preActivity == null) {
            return;
        }
        View curContentView = WindowHelper.instance().getContentView(curActivity);
        View preContentView = WindowHelper.instance().getContentView(preActivity);
        if (curContentView == null || preContentView == null) {
            return;
        }
        int screenWidth = WindowHelper.instance().getScreenWidth(curActivity);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                if (mDownX > mSlideX) return;//按下边缘位置才允许滑动
                mDownTime = System.currentTimeMillis();
                preContentView.setX((float) (-screenWidth * 0.3));
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDownX > mSlideX) return;//按下边缘位置才允许滑动
                mMoveX = ev.getX();
                if (mMoveX > mMaxMoveX) mMaxMoveX = mMoveX;
                if (mMoveX > (screenWidth - mSlideX / 2)) {
                    preContentView.setX(0);
                    curActivity.finish();
                    return;
                }
                float distance = mMoveX - mDownX;
                if (distance < 0) distance = 0;
                curContentView.setX(distance);

                float preDistance = (float) (-screenWidth * 0.3 + distance / 3);
                if (preDistance > 0) preDistance = 0;
                preContentView.setX(preDistance);
                break;
            case MotionEvent.ACTION_UP:
                if (mDownX > mSlideX) return;//按下边缘位置才允许滑动
                float upX = ev.getX();
                if (upX < mMaxMoveX && (mMaxMoveX - upX) / (System.currentTimeMillis() - mDownTime) > 0.1) {//快速向左滑动
                    curContentView.setX(0);
                } else if ((upX - mDownX) / (System.currentTimeMillis() - mDownTime) > 0.5) {//快速向右滑动
                    curActivity.finish();
                } else if (upX > screenWidth / 2) {//大于屏幕一半就结束当前Activity，根据自己需求而定吧
                    curActivity.finish();
                } else {
                    curContentView.setX(0);
                }
                preContentView.setX(0);
                mMaxMoveX = 0;
                break;
        }
    }
}
