package com.aspsine.swipetoloadlayout.demo.view.footer;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.GoogleCircleProgressView;

/**
 * Created by aspsine on 16/1/27.
 */
public class GoogleCircleHookLoadMoreFooterView extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private GoogleCircleProgressView progressView;

    private int mTriggerOffset;

    private int mFinalOffset;

    public GoogleCircleHookLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public GoogleCircleHookLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleCircleHookLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_google);
        mFinalOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_final_offset_google);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressView = (GoogleCircleProgressView) findViewById(R.id.googleProgress);
        progressView.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_yellow,
                R.color.google_green);
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onLoadMore() {
        progressView.start();
    }

    @Override
    public void onPrepare() {
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        float alpha = -y / (float) mTriggerOffset;
        ViewCompat.setAlpha(progressView, alpha);
        if (!isComplete) {
            progressView.setProgressRotation(-y / (float) mFinalOffset);
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        progressView.stop();
        ViewCompat.setAlpha(progressView, 1f);
    }

}
