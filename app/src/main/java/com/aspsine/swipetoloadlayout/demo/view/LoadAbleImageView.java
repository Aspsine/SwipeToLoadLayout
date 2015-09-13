package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.LoadMoreAble;
import com.aspsine.swipetoloadlayout.RefreshAble;

/**
 * Created by Aspsine on 2015/9/11.
 */
public class LoadAbleImageView extends ImageView implements RefreshAble, LoadMoreAble {
    public LoadAbleImageView(Context context) {
        super(context);
    }

    public LoadAbleImageView(Context context, AttributeSet attrs) {
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
