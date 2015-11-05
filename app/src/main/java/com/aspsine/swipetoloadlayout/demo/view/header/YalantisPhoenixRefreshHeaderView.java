package com.aspsine.swipetoloadlayout.demo.view.header;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.util.DensityUtil;
import com.aspsine.swipetoloadlayout.demo.view.drawable.yalantis.BaseRefreshDrawable;
import com.aspsine.swipetoloadlayout.demo.view.drawable.yalantis.SunRefreshDrawable;

/**
 * Created by Aspsine on 2015/11/5.
 */
public class YalantisPhoenixRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {

    private ImageView ivRefresh;

    private BaseRefreshDrawable mDrawable;

    private int mTriggerOffset;

    private int mFinalDragOffset;

    public YalantisPhoenixRefreshHeaderView(Context context) {
        this(context, null);
    }

    public YalantisPhoenixRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YalantisPhoenixRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_yalantis);
        mFinalDragOffset = getResources().getDimensionPixelOffset(R.dimen.refresh_final_offset_yalantis);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        mDrawable = new SunRefreshDrawable(getContext(), this, mTriggerOffset, DensityUtil.getScreenWidth(getContext()));
        ivRefresh.setBackgroundDrawable(mDrawable);
    }


    @Override
    public void onRefresh() {
        mDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    int oldY = 0;

    @Override
    public void onSwipe(int y) {
        int delta = y - oldY;
        Log.i("tag", "delta = "+ delta);
        mDrawable.offsetTopAndBottom(delta);
        mDrawable.setPercent(y / (float) mTriggerOffset, true);
        oldY = y;
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onReset() {
        mDrawable.stop();
    }
}
