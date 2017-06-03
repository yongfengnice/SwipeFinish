# SwipeFinish
右滑动退出当前Activity，类似微信滑动返回的效果，带联动效果，代码量极少，可用性高

效果图如下：

demo里面的效果：

![](http://i.imgur.com/2HBwqko.gif)

集成到真实项目的效果：

![](http://i.imgur.com/2BxiDV1.gif)

使用步骤：
	
	1.AndroidManifest.xml文件里给Activity添加theme主题
	<activity
    	android:name=".NextActivity"
        android:theme="@style/AppTheme.Translucent.NoTitleBar">
    </activity>
	
	<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    </style>
	
	2.styles.xml文件添加主题
    <style name="AppTheme.Translucent">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:colorBackgroundCacheHint">@null</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowAnimationStyle">@android:style/Animation.Activity</item>
    </style>

    <style name="AppTheme.Translucent.NoTitleBar">
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowContentOverlay">@null</item>
    </style>

	3.Application注册Activity生命周期函数
	registerActivityLifecycleCallbacks(ActivityLifeHelper.instance());

	4.在需要滑动的Activity添加如下代码，如果整体App都需要的话，可以在自定义的BaseActivity添加
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean processTouchEvent = SwipeHelper.instance().processTouchEvent(ev);
        if (processTouchEvent) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }