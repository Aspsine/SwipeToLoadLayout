package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/11.
 */
public class LoadAbleGridView extends GridView implements RefreshAble, LoadMoreAble {
    public LoadAbleGridView(Context context) {
        super(context);
    }

    public LoadAbleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAbleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onCheckCanLoadMore() {
        return canChildScrollUp();
    }

    @Override
    public boolean onCheckCanRefresh() {
        return canChildScrollDown();
    }

    public boolean canChildScrollDown() {
        if (getCount() == 0 ||
                (getFirstVisiblePosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() >= 0)) {
            return true;
        }
        return false;
    }

    public boolean canChildScrollUp() {
        int lastVisiblePosition = getLastVisiblePosition();
        View lastView = getChildAt(getChildCount() - 1);
        if (lastVisiblePosition == getCount() - 1 && lastView != null && lastView.getBottom() >= 0) {
            return true;
        }
        return false;
    }
}
