package com.swipefinish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.swipefinish.helper.SwipeHelper;

import java.util.Random;

public class NextActivity extends Activity {
    public static int mIndex = 0;
    private TextView mTextView;

    private void assignViews() {
        Random random = new Random();
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setBackgroundColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        mTextView.setText("我是第" + (++mIndex) + "页");
    }

    public void click(View view) {
        startActivity(new Intent(this, NextActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        assignViews();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        SwipeHelper.instance().processTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

}
