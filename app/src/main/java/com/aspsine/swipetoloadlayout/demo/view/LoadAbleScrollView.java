package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class LoadAbleScrollView extends ScrollView implements RefreshAble, LoadMoreAble {


    public LoadAbleScrollView(Context context) {
        super(context);
    }

    public LoadAbleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAbleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onCheckCanLoadMore() {
        return false;
    }

    @Override
    public boolean onCheckCanRefresh() {
        return false;
    }


}
