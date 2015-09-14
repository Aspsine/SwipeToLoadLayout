package com.aspsine.swipetoloadlayout.demo.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;

/**
 * Created by aspsine on 15/9/10.
 */
public class GoogleRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ProgressBar progressBar;

    private int mHeaderHeight;

    public GoogleRefreshHeaderView(Context context) {
        this(context, null);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_google);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(mHeaderHeight);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int y) {
        progressBar.setProgress(y);
    }

    @Override
    public void complete() {

    }

    @Override
    public void onReset() {

    }
}
