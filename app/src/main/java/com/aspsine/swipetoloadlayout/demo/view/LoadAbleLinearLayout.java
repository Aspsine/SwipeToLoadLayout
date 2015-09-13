package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/11.
 */
public class LoadAbleLinearLayout extends LinearLayout implements RefreshAble, LoadMoreAble {
    public LoadAbleLinearLayout(Context context) {
        super(context);
    }

    public LoadAbleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onCheckCanLoadMore() {
        return true;
    }

    @Override
    public boolean onCheckCanRefresh() {
        return true;
    }

}
