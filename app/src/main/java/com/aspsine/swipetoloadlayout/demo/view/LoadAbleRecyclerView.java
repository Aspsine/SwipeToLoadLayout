package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class LoadAbleRecyclerView extends RecyclerView implements RefreshAble, LoadMoreAble {
    public LoadAbleRecyclerView(Context context) {
        super(context);
    }

    public LoadAbleRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAbleRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
        if (manager.getChildCount() == 0 ||
                (manager.findFirstCompletelyVisibleItemPosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() >= 0)) {
            return true;
        }
        return false;
    }

    public boolean canChildScrollUp() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        if (lastVisibleItem >= itemCount - 1) {
            return true;
        }
        return false;
    }
}