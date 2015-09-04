package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class LoadAbleListView extends ListView implements RefreshAble, LoadMoreAble {

    private boolean mRefreshAble = true;

    private boolean mLoadMoreAble;

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
        return false;
    }

    @Override
    public boolean onCheckCanRefresh() {
        return canChildScrollUp() && mRefreshAble;
    }

    public boolean canChildScrollUp() {
        if (getCount() == 0 ||
                (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0)) {
            return true;
        }
        return false;
    }
}
