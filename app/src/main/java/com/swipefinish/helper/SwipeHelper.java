package com.swipefinish.helper;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by yongfeng on 2016/11/27.
 * Email:2499522170@qq.com
 */
public class SwipeHelper {
    private final int START_SLIDE_X = 30;

    private static SwipeHelper sSwipeHelper;
    private float mDownX;//按下的位置
    private float mMoveX;//当前滑动位置
    private float mMaxMoveX;//最大的滑动位置
    private long mDownTime;//按下的时间点

    private float mScreenWidth;
    private float mPreContentViewX;//最大的前一个View的X位置,也就是在屏幕左侧的最大值

    private ValueAnimator mPreContentViewAnimator;
    private ValueAnimator mCurContentViewAnimator;

    private SwipeHelper() {
        mPreContentViewAnimator = ValueAnimator.ofFloat(0, 0);
        mPreContentViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mPreContentViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                View preContentView = getPreContentView();
                if (value instanceof Float && preContentView != null) {
                    float aFloat = Float.parseFloat(value.toString());
                    if (aFloat <= mPreContentViewX) {//说明滑动结束，恢复0值
                        aFloat = 0;
                    }
                    preContentView.setX(aFloat);
                }
            }
        });

        mCurContentViewAnimator = ValueAnimator.ofFloat(0, 0);
        mCurContentViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mCurContentViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                View curContentView = getCurContentView();
                if (value instanceof Float && curContentView != null) {
                    float aFloat = Float.parseFloat(value.toString());
                    curContentView.setX(aFloat);
                    if (aFloat >= mScreenWidth) {
                        ActivityLifeHelper.instance().getCurrentActivity().finish();
                    }
                }
            }
        });
    }

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


    private View getCurContentView() {
        Activity curActivity = ActivityLifeHelper.instance().getCurrentActivity();
        if (curActivity != null) {
            return WindowHelper.instance().getContentView(curActivity);
        }
        return null;
    }

    private View getPreContentView() {
        Activity preActivity = ActivityLifeHelper.instance().getPreviewActivity();
        if (preActivity != null) {
            return WindowHelper.instance().getContentView(preActivity);
        }
        return null;
    }

    public boolean processTouchEvent(MotionEvent ev) {
        if (mCurContentViewAnimator.isRunning() || mPreContentViewAnimator.isRunning()) {
            return true;
        }

        View curContentView = getCurContentView();
        View preContentView = getPreContentView();
        if (curContentView == null || preContentView == null) {
            return false;
        }

        Activity curActivity = ActivityLifeHelper.instance().getCurrentActivity();
        if (mScreenWidth <= 0) {
            mScreenWidth = WindowHelper.instance().getScreenWidth(curActivity);
            mPreContentViewX = -mScreenWidth * 0.3f;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                if (mDownX > START_SLIDE_X) {
                    return false;//按下边缘位置才允许滑动
                }
                mDownTime = System.currentTimeMillis();
                preContentView.setX(mPreContentViewX);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDownX > START_SLIDE_X) {
                    return false;//按下边缘位置才允许滑动
                }
                mMoveX = ev.getX();
                if (mMoveX > mMaxMoveX) mMaxMoveX = mMoveX;
                if (mMoveX > (mScreenWidth - START_SLIDE_X / 2)) {//滑到最右端
                    preContentView.setX(0);
                    curActivity.finish();
                    return true;
                }
                //移动当前View
                float distance = mMoveX - mDownX;
                if (distance < 0) {
                    distance = 0;
                }
                curContentView.setX(distance);

                //移动之前View
                float preDistance = mPreContentViewX + distance / 3;
                if (preDistance > 0) {
                    preDistance = 0;
                }
                preContentView.setX(preDistance);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDownX > START_SLIDE_X) {
                    return false;//按下边缘位置才允许滑动
                }
                float upX = ev.getX();
                if (upX < mMaxMoveX && (mMaxMoveX - upX) / (System.currentTimeMillis() - mDownTime) > 0.1) {//快速向左滑动
                    animateCurContentView(curContentView.getX(), 0);
                    animatePreContentView(preContentView.getX(), mPreContentViewX);
                } else if ((upX - mDownX) / (System.currentTimeMillis() - mDownTime) > 0.5//快速向右滑动
                        || upX > mScreenWidth / 2) {//大于屏幕一半就结束当前Activity，根据自己需求而定吧
                    animateCurContentView(curContentView.getX(), mScreenWidth);
                    animatePreContentView(preContentView.getX(), 0);
                } else {
                    animateCurContentView(curContentView.getX(), 0);
                    animatePreContentView(preContentView.getX(), mPreContentViewX);
                }
                mMaxMoveX = 0;
                break;
        }
        return true;
    }

    private void animatePreContentView(float start, float end) {
        mPreContentViewAnimator.setFloatValues(start, end);
        mPreContentViewAnimator.start();
    }

    private void animateCurContentView(float start, float end) {
        mCurContentViewAnimator.setFloatValues(start, end);
        mCurContentViewAnimator.start();
    }
}
