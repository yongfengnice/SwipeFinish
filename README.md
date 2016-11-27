# SwipeFinish
右滑动退出当前Activity，类似微信滑动返回的效果，带联动效果，代码量极少，可用性高

不上视频展示效果了，具体效果自己fork代码下了运行看看就知道了。

	使用方法，重写Activity的dispatchTouchEvent方法，
	调用SwipeHelper.instance().processTouchEvent(ev)，一句话搞定：
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        SwipeHelper.instance().processTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

