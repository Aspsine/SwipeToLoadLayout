package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by aspsine on 15/9/10.
 */
public class GoogleRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    public GoogleRefreshHeaderView(Context context) {
        super(context);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int y) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onReset() {

    }
}
