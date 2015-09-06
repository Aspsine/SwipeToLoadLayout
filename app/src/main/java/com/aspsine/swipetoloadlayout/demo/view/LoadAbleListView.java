package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class LoadAbleListView extends ListView implements RefreshAble, LoadMoreAble {

    private boolean mRefreshAble = true;

    private boolean mLoadMoreAble = true;

    public LoadAbleListView(Context context) {
        super(context);
    }

    public LoadAbleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAbleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRefreshAble(boolean mRefreshAble) {
        this.mRefreshAble = mRefreshAble;
    }

    public void setLoadMoreAble(boolean mLoadMoreAble) {
        this.mLoadMoreAble = mLoadMoreAble;
    }

    @Override
    public boolean onCheckCanLoadMore() {
        return canChildScrollUp() && mLoadMoreAble;
    }

    @Override
    public boolean onCheckCanRefresh() {
        return canChildScrollDown() && mRefreshAble;
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
