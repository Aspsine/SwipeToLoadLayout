package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/11.
 */
public class LoadAbleRelativeLayout extends RelativeLayout implements RefreshAble, LoadMoreAble {
    public LoadAbleRelativeLayout(Context context) {
        super(context);
    }

    public LoadAbleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAbleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onCheckCanLoadMore() {
        return true;
    }

    @Override
    public boolean onCheckCanRefresh() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
