package com.aspsine.swipetoloadlayout.demo.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.drawable.RingProgressDrawable;

/**
 * Created by aspsine on 15/9/10.
 */
public class GoogleRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ImageView ivRefresh;

    private int mHeaderHeight;

    private RingProgressDrawable ringProgressDrawable;

    public GoogleRefreshHeaderView(Context context) {
        this(context, null);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ringProgressDrawable = new RingProgressDrawable(context);
        mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_google);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivRefresh.setBackgroundDrawable(ringProgressDrawable);
    }

    @Override
    public void onRefresh() {
        ringProgressDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int y) {
        ringProgressDrawable.setProgress(y / ((float) mHeaderHeight));
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onReset() {

    }
}
