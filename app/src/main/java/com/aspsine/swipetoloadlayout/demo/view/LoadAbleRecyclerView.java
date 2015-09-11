package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

        LayoutManager manager = getLayoutManager();
        int firstVisibleItem = 0;
        if (manager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            int[] array = ((StaggeredGridLayoutManager) manager).findFirstCompletelyVisibleItemPositions(null);
            int length = array.length;
            if (length > 0) {
                firstVisibleItem = array[0];
            }
        }
        if (getChildCount() == 0 ||
                (firstVisibleItem == 0 && getChildAt(0) != null && getChildAt(0).getTop() >= 0)) {
            return true;
        }
        return false;
    }

    public boolean canChildScrollUp() {
        LayoutManager manager = getLayoutManager();
        int itemCount = manager.getItemCount();
        int lastVisibleItem = 0;
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            int[] array = layoutManager.findLastCompletelyVisibleItemPositions(null);
            int length = array.length;
            if (length > 0) {
                lastVisibleItem = array[length - 1];
            }
        }
        if (lastVisibleItem >= itemCount - 1) {
            return true;
        }
        return false;
    }
}